package dk.hjemmehub.soundwavebackend.Service;

import dk.hjemmehub.soundwavebackend.DTO.SeatDto;
import dk.hjemmehub.soundwavebackend.Model.EventSeat;
import dk.hjemmehub.soundwavebackend.DTO.SeatReservationRequest;
import dk.hjemmehub.soundwavebackend.Repository.EventSeatRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import dk.hjemmehub.soundwavebackend.Model.Area;
import dk.hjemmehub.soundwavebackend.Model.Seat;
import dk.hjemmehub.soundwavebackend.Repository.SeatRepository;
import dk.hjemmehub.soundwavebackend.Repository.AreaRepository;
import dk.hjemmehub.soundwavebackend.Repository.EventRepository;
import dk.hjemmehub.soundwavebackend.DTO.SeatMapDto;
import dk.hjemmehub.soundwavebackend.DTO.AreaMapDto;
import dk.hjemmehub.soundwavebackend.DTO.EventMapDto;

import java.util.List;

@Service
public class SeatService {

    private final EventSeatRepository eventSeatRepository;
    private final SeatRepository seatRepository;
    private final AreaRepository areaRepository;
    private final EventRepository eventRepository;

    public SeatService(EventSeatRepository eventSeatRepository, SeatRepository seatRepository, AreaRepository areaRepository, EventRepository eventRepository) {
      this.eventSeatRepository = eventSeatRepository;
      this.seatRepository = seatRepository;
      this.areaRepository = areaRepository;
      this.eventRepository = eventRepository;
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


    public List<SeatDto> getSeatsForEvent(Long eventId) {
        List<EventSeat> eventSeats = eventSeatRepository.findByEvent_EventId(eventId);
        return eventSeats.stream()
                .map(es -> new SeatDto(
                        es.getSeat().getSeatId(),
                        es.getSeat().getRowNumber(),
                        es.getSeat().getSeatNumber(),
                        es.isReserved() ? "booked" : "free",
                        toRowLetter(es.getSeat().getRowNumber()) + es.getSeat().getSeatNumber()
                ))
                .toList();
    }

    @Transactional
    public void reserveSeat(Long eventId, Long seatId) {
        var matches = eventSeatRepository.findByEvent_EventIdAndSeat_SeatIdIn(eventId, List.of(seatId));
        if (matches.isEmpty()) {
            throw new IllegalArgumentException("Seat not part of event");
        }
        EventSeat es = matches.get(0);
        if (es.isReserved()) {
            throw new IllegalStateException("Seat already reserved");
        }
        es.setReserved(true);
        eventSeatRepository.save(es);
    }

    @Transactional
    public void reserveSeatsBulk(Long eventId, SeatReservationRequest request) {
        List<Long> seatIds = request.getSeatIds();
        var seats = eventSeatRepository.findByEvent_EventIdAndSeat_SeatIdIn(eventId, seatIds);

        if (seats.size() != seatIds.size()) {
            throw new IllegalArgumentException("One or more seats are not part of the event");
        }

        for (EventSeat es : seats) {
            if (es.isReserved()) {
                throw new IllegalStateException("One or more seats already reserved");
            }
            es.setReserved(true);
        }
        eventSeatRepository.saveAll(seats);
    }

    // Hjælper til labels A1, B3 osv.
    private String toRowLabel(int rowNumber, int seatNumber) {
        return (char) ('A' + rowNumber - 1) + String.valueOf(seatNumber);
    }

    @Transactional
    public void setupSeats(Long areaId, int rows, int cols) {
        Area area = areaRepository.findById(areaId)
                .orElseThrow(() -> new IllegalArgumentException("Area not found"));

        // refuse to create if seats already exist for area
        if (seatRepository.existsByArea_AreaId(areaId)) {
            throw new IllegalStateException("Seats already exist for this area. Use overwrite to recreate.");
        }

        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= cols; c++) {
                Seat seat = new Seat();
                seat.setRowNumber(r);
                seat.setSeatNumber(c);
                seat.setArea(area);
                seatRepository.save(seat);
            }
        }
    }

    public EventMapDto buildEventMap(Long eventId) {
        var event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found"));

        List<EventSeat> eventSeats = eventSeatRepository.findByEvent_EventId(eventId);

        // group seats by their Area
        var seatsByArea = eventSeats.stream().collect(java.util.stream.Collectors.groupingBy(es -> es.getSeat().getArea()));

        var areaDtos = seatsByArea.entrySet().stream().map(entry -> {
            Area area = entry.getKey();
            List<EventSeat> seatsForArea = entry.getValue();

            int bookedCount = (int) seatsForArea.stream().filter(EventSeat::isReserved).count();
            Double price = event.getBasePrice() != null ? event.getBasePrice().doubleValue() : 0.0;

            List<SeatMapDto> seatDtos = seatsForArea.stream()
                    .map(es -> {
                        Seat s = es.getSeat();
                        String status = es.isReserved() ? "booked" : "free";
                        String label = toRowLetter(s.getRowNumber()) + s.getSeatNumber();
                        return new SeatMapDto(s.getSeatId(), s.getRowNumber(), s.getSeatNumber(), status, label, area.getAreaId(), price);
                    })
                    .sorted(java.util.Comparator.comparingInt(SeatMapDto::getRowNumber).thenComparingInt(SeatMapDto::getSeatNumber))
                    .toList();

            return new AreaMapDto(area.getAreaId(), area.getName(), area.getType(), area.getCapacity(), bookedCount, price, seatDtos);
        }).toList();

        String hallName = "";
        if (!areaDtos.isEmpty()) {
            hallName = areaDtos.get(0).getAreaName(); // fallback - real hall name not modelled on Event
        }

        return new EventMapDto(event.getEventId(), event.getTitle(), hallName, areaDtos);
    }


}
