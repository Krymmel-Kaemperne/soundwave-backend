package dk.hjemmehub.soundwavebackend.Service;

import dk.hjemmehub.soundwavebackend.DTO.ReservationConfirmationDto;
import dk.hjemmehub.soundwavebackend.DTO.ReservationRequestDto;
import dk.hjemmehub.soundwavebackend.DTO.StandingDto;
import dk.hjemmehub.soundwavebackend.Model.Area;
import dk.hjemmehub.soundwavebackend.Model.EventSeat;
import dk.hjemmehub.soundwavebackend.Model.Reservation;
import dk.hjemmehub.soundwavebackend.Repository.AreaRepository;
import dk.hjemmehub.soundwavebackend.Repository.EventSeatRepository;
import dk.hjemmehub.soundwavebackend.Repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private EventSeatRepository eventSeatRepository;

    @Autowired
    private AreaRepository areaRepository;

    //Create Reservation
    public ReservationConfirmationDto createReservation(ReservationRequestDto reservationRequest) {
        Reservation reservation = new Reservation();
        reservation.setEventId(reservationRequest.getEventId());
        reservation.setUserName(reservationRequest.getCustomerName());
        reservation.setUserEmail(reservationRequest.getCustomerEmail());
        reservation.setTotalPrice(BigDecimal.valueOf(reservationRequest.getTotalPrice()));

        Reservation saved = reservationRepository.save(reservation);

        // 🔧 Marker sæder som reserverede
        if (reservationRequest.getSeatIds() != null && !reservationRequest.getSeatIds().isEmpty()) {
            for (Long seatId : reservationRequest.getSeatIds()) {
                List<EventSeat> seats = eventSeatRepository.findByEvent_EventIdAndSeat_SeatIdIn(
                        reservationRequest.getEventId(), List.of(seatId)
                );

                if (seats.isEmpty()) {
                    throw new RuntimeException(
                            "Seat not found for eventId " + reservationRequest.getEventId() + " and seatId " + seatId);
                }

                EventSeat eventSeat = seats.get(0);
                if (eventSeat.isReserved()) {
                    throw new RuntimeException("Seat " + seatId + " is already reserved!");
                }

                eventSeat.setReserved(true);
                eventSeatRepository.save(eventSeat);
            }
        }

        // 🔧 Fjern antallet af ståpladser
        if (reservationRequest.getStandingAreas() != null && !reservationRequest.getStandingAreas().isEmpty()) {
            for (StandingDto standingDto : reservationRequest.getStandingAreas()) {

                Area area = areaRepository.findById(standingDto.getAreaId())
                        .orElseThrow(() -> new RuntimeException("Area not found: " + standingDto.getAreaId()));

                // 🔧 Undgå NullPointerException hvis nogle felter er null
                int count = standingDto.getCount() != null ? standingDto.getCount() : 0;

                if (area.getCapacity() < count) {
                    throw new RuntimeException(
                            "Not enough standing places left in area: " + area.getName());
                }

                area.setCapacity(area.getCapacity() - count);
                areaRepository.save(area);
            }
        }

        //Returner confirmation
        ReservationConfirmationDto response = new ReservationConfirmationDto();
        response.setCustomerName(saved.getUserName());
        response.setCustomerEmail(saved.getUserEmail());
        response.setTotalPrice(saved.getTotalPrice().doubleValue());
        response.setStatus("confirmed");

        return response;
    }

    //Man kan lave metoder til at annulere eller slette ordre
}