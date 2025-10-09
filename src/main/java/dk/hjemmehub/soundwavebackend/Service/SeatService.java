package dk.hjemmehub.soundwavebackend.Service;

import dk.hjemmehub.soundwavebackend.DTO.SeatDto;
import dk.hjemmehub.soundwavebackend.Model.EventSeat;
import dk.hjemmehub.soundwavebackend.DTO.ReservationRequestDto;
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
import java.util.stream.Collectors; // Importer Collectors
import java.util.Map; // Importer Map

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

    // CONVERTERE NUMBER TIL STRING (hjælpemetode til sædelabels)
    private String toRowLetter(int rowNumber) {
        // Tjek for ugyldige rækkenumre for at undgå fejl
        if (rowNumber < 1 || rowNumber > 26) {
            return String.valueOf(rowNumber); // Returnerer bare tallet, hvis udenfor 'A'-'Z'
        }
        return String.valueOf((char) ('A' + rowNumber - 1));
    }


    public List<SeatDto> getSeatsForEvent(Long eventId) {
        List<EventSeat> eventSeats = eventSeatRepository.findByEvent_EventId(eventId);
        return eventSeats.stream()
                .filter(es -> es.getSeat() != null) // Sørg for at filtrere null Seats (f.eks. for standing)
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
    public void reserveSeatsBulk(Long eventId, ReservationRequestDto request) {
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

    @Transactional
    public void generateSeatsForEvent(Long eventId) {
        var event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        // Get all areas for this event's hall
        List<Area> areas = areaRepository.findByHall_HallId(event.getHall().getHallId());

        for (Area area : areas) {
            if (area.getType().equals("seating")) {
                // Check if seats already exist for this area
                if (!seatRepository.existsByArea_AreaId(area.getAreaId())) {
                    // Create seats for the area if they don't exist
                    int rows = getRowCountForArea(area.getName());
                    int cols = getSeatsPerRowForArea(area.getName());
                    setupSeats(area.getAreaId(), rows, cols);
                }

                // Create EventSeat records linking this event to all seats in the area
                List<Seat> seats = seatRepository.findByArea_AreaId(area.getAreaId());
                for (Seat seat : seats) {
                    // Check if EventSeat already exists
                    if (!eventSeatRepository.existsByEvent_EventIdAndSeat_SeatId(eventId, seat.getSeatId())) {
                        EventSeat eventSeat = new EventSeat();
                        eventSeat.setEvent(event);
                        eventSeat.setSeat(seat);
                        eventSeat.setReserved(false);
                        eventSeatRepository.save(eventSeat);
                    }
                }
            }
        }
    }

    private int getRowCountForArea(String areaName) {
        if (areaName.contains("Bag")) return 6;  // Back area has 6 rows
        return 2;  // Left and right areas have 2 rows
    }

    private int getSeatsPerRowForArea(String areaName) {
        if (areaName.contains("Bag")) return 15;  // Back area has 15 seats per row
        return 10;  // Left and right areas have 10 seats per row
    }

    // Hjælper til labels A1, B3 osv. - Denne metode er ikke brugt her, men du har den.
    private String toRowLabel(int rowNumber, int seatNumber) {
        return toRowLetter(rowNumber) + String.valueOf(seatNumber);
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


    //Used for pricemodifier
    private boolean isBalcony(String areaName) {
        if (areaName == null) return false;
        String n = areaName.toLowerCase();
        return n.contains("balcony") || n.contains("balkon");
    }

    public EventMapDto buildEventMap(Long eventId) {
        var event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        List<EventSeat> allEventSeats = eventSeatRepository.findByEvent_EventId(eventId);

        // Group actual seating EventSeats by their Area for easier processing
        Map<Area, List<EventSeat>> seatingSeatsByArea = allEventSeats.stream()
                .filter(es -> es.getSeat() != null && es.getSeat().getArea() != null)
                .collect(Collectors.groupingBy(es -> es.getSeat().getArea()));

        // Get all areas associated with the event's hall, including standing areas
        List<Area> allAreasInHall =
                areaRepository.findByHall_HallId(event.getHall().getHallId());

        var areaMapDtos = allAreasInHall.stream().map(area -> {
            int bookedCountForArea;

            Double priceForArea = event.getBasePrice() != null
                    ? event.getBasePrice().doubleValue()
                    : 0.0;

            if ("seating".equals(area.getType()) && isBalcony(area.getName())) {
                priceForArea += 200.0;
            }

            final double areaPrice = priceForArea;

            List<SeatMapDto> seatDtosForArea;

            if ("standing".equals(area.getType())) {
                // Count standing reservations (EventSeats with seat == null)
                bookedCountForArea = (int) allEventSeats.stream()
                        .filter(es -> es.getSeat() == null && es.isReserved())
                        .count();

                seatDtosForArea = List.of(); // no individual seats
            } else {
                List<EventSeat> seatingEventSeats =
                        seatingSeatsByArea.getOrDefault(area, List.of());
                bookedCountForArea =
                        (int) seatingEventSeats.stream().filter(EventSeat::isReserved).count();

                seatDtosForArea = seatingEventSeats.stream()
                        .filter(es -> es.getSeat() != null)
                        .map(es -> {
                            Seat s = es.getSeat();
                            String status = es.isReserved() ? "booked" : "free";
                            String label = toRowLetter(s.getRowNumber()) + s.getSeatNumber();
                            return new SeatMapDto(
                                    s.getSeatId(),
                                    s.getRowNumber(),
                                    s.getSeatNumber(),
                                    status,
                                    label,
                                    area.getAreaId(),
                                    areaPrice // use the final copy
                            );
                        })
                        .sorted(java.util.Comparator
                                .comparingInt(SeatMapDto::getRowNumber)
                                .thenComparingInt(SeatMapDto::getSeatNumber))
                        .toList();
            }

            return new AreaMapDto(
                    area.getAreaId(),
                    area.getName(),
                    area.getType(),
                    area.getCapacity(),
                    bookedCountForArea,
                    areaPrice,
                    seatDtosForArea
            );
        }).toList();

        String hallName = event.getHall() != null ? event.getHall().getName() : "Ukendt Hal";
        if (hallName.isEmpty() && !areaMapDtos.isEmpty()) {
            hallName = areaMapDtos.get(0).getAreaName();
        }

        return new EventMapDto(event.getEventId(), event.getTitle(), hallName, areaMapDtos, event.getIsVisible());
    }
}