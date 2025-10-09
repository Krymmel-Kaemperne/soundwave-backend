package dk.hjemmehub.soundwavebackend.Service;

import dk.hjemmehub.soundwavebackend.DTO.ReservationConfirmationDto;
import dk.hjemmehub.soundwavebackend.DTO.ReservationRequestDto;
import dk.hjemmehub.soundwavebackend.DTO.StandingDto;
import dk.hjemmehub.soundwavebackend.Model.*;

import dk.hjemmehub.soundwavebackend.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.List;

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

    @Transactional
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
        if (request.getSeatIds() != null && !request.getSeatIds().isEmpty()) {
            for (Long seatId : request.getSeatIds()) {

                // Tjek at sædet rent faktisk eksisterer
                Seat seat = seatRepository.findById(seatId)
                        .orElseThrow(() -> new RuntimeException("Seat not found: " + seatId));

                // Find eventSeat
                EventSeat eventSeat = eventSeatRepository
                        .findByEvent_EventIdAndSeat_SeatId(request.getEventId(), seatId)
                        .orElseThrow(() -> new RuntimeException("EventSeat not found for seat: " + seatId));

                // ✅ KUN tjek for permanent BOOKED - HELD er OK at konvertere
                if ("BOOKED".equals(eventSeat.getStatus())) {
                    throw new RuntimeException("Seat " + seatId + " is already permanently booked!");
                }

                // Konverter HELD eller FREE til BOOKED
                eventSeat.setStatus("BOOKED");
                eventSeat.setHeldUntil(null);  // Ryd hold-data
                eventSeat.setSessionId(null);  // Ryd session
                eventSeatRepository.save(eventSeat);

                // Gem koblingen i Reservation_Seat
                ReservationSeat rs = new ReservationSeat();
                rs.setReservationId(saved.getReservationId());
                rs.setSeatId(seatId);
                reservationSeatRepository.save(rs);
            }
        }

        // 3️⃣ Gem ståpladser
        if (request.getStandingAreas() != null && !request.getStandingAreas().isEmpty()) {
            for (StandingDto standingDto : request.getStandingAreas()) {
                if (standingDto.getCount() == null || standingDto.getCount() <= 0) {
                    continue;
                }

                // Find de ledige "EventSeat" records for ståpladser
                List<EventSeat> availableSpots = eventSeatRepository.findAvailableStandingSpots(
                        request.getEventId(),
                        standingDto.getAreaId(),
                        standingDto.getCount()
                );

                if (availableSpots.size() < standingDto.getCount()) {
                    throw new RuntimeException("Ikke nok ledige ståpladser for area: " + standingDto.getAreaId());
                }

                // Opdater status til BOOKED
                for (EventSeat spot : availableSpots) {
                    spot.setStatus("BOOKED");
                }
                eventSeatRepository.saveAll(availableSpots);

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