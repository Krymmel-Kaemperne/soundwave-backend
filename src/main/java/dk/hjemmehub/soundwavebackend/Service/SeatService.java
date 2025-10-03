package dk.hjemmehub.soundwavebackend.Service;

import dk.hjemmehub.soundwavebackend.DTO.SeatDto;
import dk.hjemmehub.soundwavebackend.Model.EventSeat;
import dk.hjemmehub.soundwavebackend.Repository.EventSeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {

    private final EventSeatRepository eventSeatRepository;

    public SeatService(EventSeatRepository eventSeatRepository) {
      this.eventSeatRepository = eventSeatRepository;
    }

    //DEN RIGTIGE!!!
//    public List<SeatDto> getSeatsForEvent(Long eventId) {
//        List<EventSeat> eventSeats = eventSeatRepository.findByEvent_EventId(eventId);
//        return eventSeats.stream()
//                .map(es -> new SeatDto(
//                        es.getSeat().getSeatId(),
//                        es.getSeat().getRowNumber(),
//                        es.getSeat().getSeatNumber(),
//                        es.isReserved() ? "booked" : "free",
//                        toRowLetter(es.getSeat().getRowNumber()) + es.getSeat().getSeatNumber()
//                ))
//                .toList();
//    }

    //CONVERTERE NUMBER TIL STRING
    private String toRowLetter(int rowNumber) {
        return String.valueOf((char) ('A' + rowNumber - 1));
    }


    // 🧪 FAKE DATA til test (ser ud som DB’en ville give)
    public List<SeatDto> getSeatsForEvent(Long eventId) {
        // Simuler at vi har 2 rækker á 5 seats
        return List.of(
                new SeatDto(1L, 1, 1, "free", "A1"),
                new SeatDto(2L, 1, 2, "booked", "A2"),
                new SeatDto(3L, 1, 3, "free", "A3"),
                new SeatDto(4L, 1, 4, "free", "A4"),
                new SeatDto(5L, 1, 5, "booked", "A5"),

                new SeatDto(6L, 2, 1, "free", "B1"),
                new SeatDto(7L, 2, 2, "booked", "B2"),
                new SeatDto(8L, 2, 3, "free", "B3"),
                new SeatDto(9L, 2, 4, "free", "B4"),
                new SeatDto(10L, 2, 5, "booked", "B5")
        );
    }

    // Hjælper til labels A1, B3 osv.
    private String toRowLabel(int rowNumber, int seatNumber) {
        return (char) ('A' + rowNumber - 1) + String.valueOf(seatNumber);
    }


}
