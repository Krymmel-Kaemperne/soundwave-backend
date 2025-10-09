package dk.hjemmehub.soundwavebackend.Service;

import dk.hjemmehub.soundwavebackend.DTO.ReservationConfirmationDto;
import dk.hjemmehub.soundwavebackend.DTO.ReservationRequestDto;
import dk.hjemmehub.soundwavebackend.DTO.StandingDto;
import dk.hjemmehub.soundwavebackend.Model.*;

import dk.hjemmehub.soundwavebackend.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private EventSeatRepository eventSeatRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private ReservationSeatRepository reservationSeatRepository;

    @Autowired
    private ReservationAreaRepository reservationAreaRepository;

    public ReservationConfirmationDto createReservation(ReservationRequestDto request) {

        System.out.println("=== CREATE RESERVATION DEBUG ===");
        System.out.println("EventId: " + request.getEventId());
        System.out.println("Customer: " + request.getCustomerName());
        System.out.println("SeatIds: " + request.getSeatIds());
        System.out.println("StandingAreas: " + request.getStandingAreas());

        if (request.getEventId() == null) {
            throw new RuntimeException("EventId cannot be null!");
        }




        // 1️⃣ Lav selve reservationen
        Reservation reservation = new Reservation();
        reservation.setEventId(request.getEventId());
        reservation.setUserName(request.getCustomerName());
        reservation.setUserEmail(request.getCustomerEmail());
        reservation.setTotalPrice(BigDecimal.valueOf(request.getTotalPrice()));
        reservation.setStatus("confirmed");
        Reservation saved = reservationRepository.save(reservation);

        // 2️⃣ Gem seats
        if (request.getSeatIds() != null) {
            for (Long seatId : request.getSeatIds()) {

                // --> Tjek at sædet rent faktisk eksisterer
                Seat seat = seatRepository.findById(seatId)
                        .orElseThrow(() -> new RuntimeException("Seat not found: " + seatId));

                // --> Find eventSeat og marker det reserveret
                EventSeat eventSeat = eventSeatRepository

                        .findByEvent_EventIdAndSeat_SeatId(request.getEventId(), seatId)
                        .orElseThrow(() -> new RuntimeException("EventSeat not found for seat: " + seatId));

                if (eventSeat.isReserved()) {
                    throw new RuntimeException("Seat " + seatId + " is already reserved!");
                }

                eventSeat.setReserved(true);
                eventSeatRepository.save(eventSeat);

                // --> Gem koblingen i Reservation_Seat
                ReservationSeat rs = new ReservationSeat();

                System.out.println("SeatIds from request: " + request.getSeatIds());
                System.out.println("StandingAreas from request: " + request.getStandingAreas());

                rs.setReservationId(saved.getReservationId());
                rs.setSeatId(seatId);
                reservationSeatRepository.save(rs);
            }
        }

        // 3️⃣ Gem ståpladser
        if (request.getStandingAreas() != null) {
            for (StandingDto standingDto : request.getStandingAreas()) {

                Area area = areaRepository.findById(standingDto.getAreaId())
                        .orElseThrow(() -> new RuntimeException("Area not found: " + standingDto.getAreaId()));

                if (area.getCapacity() < standingDto.getCount()) {
                    throw new RuntimeException("Not enough capacity for area: " + area.getName());
                }

                // Fjern antal ståpladser
                area.setCapacity(area.getCapacity() - standingDto.getCount());
                areaRepository.save(area);

                // Gem koblingen i Reservation_Area
                ReservationArea ra = new ReservationArea();
                ra.setReservationId(saved.getReservationId());
                ra.setAreaId(standingDto.getAreaId());
                ra.setStandingCount(standingDto.getCount());
                reservationAreaRepository.save(ra);
            }
        }

        // 4️⃣ Svar tilbage til frontend
        ReservationConfirmationDto response = new ReservationConfirmationDto();
        response.setCustomerName(saved.getUserName());
        response.setCustomerEmail(saved.getUserEmail());
        response.setTotalPrice(request.getTotalPrice());
        response.setStatus("confirmed");

        return response;
    }
}