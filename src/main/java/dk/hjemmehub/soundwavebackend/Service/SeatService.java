package dk.hjemmehub.soundwavebackend.Service;

import dk.hjemmehub.soundwavebackend.DTO.SeatDto;
import dk.hjemmehub.soundwavebackend.Model.EventSeat;
import dk.hjemmehub.soundwavebackend.DTO.SeatReservationRequest;
import dk.hjemmehub.soundwavebackend.Repository.EventSeatRepository;
// org.hibernate.Session import removed (unused after cache removal)
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

import jakarta.persistence.LockTimeoutException;
import jakarta.persistence.PessimisticLockException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; // Importer Collectors
import java.util.stream.IntStream;
import java.util.Map; // Importer Map
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class SeatService {

    private final EventSeatRepository eventSeatRepository;
    private final SeatRepository seatRepository;
    private final AreaRepository areaRepository;
    private final EventRepository eventRepository;
    private final SessionService sessionService;

    public SeatService(EventSeatRepository eventSeatRepository, SeatRepository seatRepository, AreaRepository areaRepository, EventRepository eventRepository, SessionService sessionService) {
        this.eventSeatRepository = eventSeatRepository;
        this.seatRepository = seatRepository;
        this.areaRepository = areaRepository;
        this.eventRepository = eventRepository;
        this.sessionService = sessionService;
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
                        ("BOOKED".equals(es.getStatus()) || "HELD".equals(es.getStatus())) ? "booked" : "free",
                        toRowLetter(es.getSeat().getRowNumber()) + es.getSeat().getSeatNumber()
                ))
                .toList();
    }

    // Paginated version for large datasets
    public Page<SeatDto> getSeatsForEventPaginated(Long eventId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EventSeat> eventSeatsPage = eventSeatRepository.findByEvent_EventId(eventId, pageable);
        
        return eventSeatsPage.map(es -> {
            if (es.getSeat() == null) return null; // Skip null seats
            return new SeatDto(
                    es.getSeat().getSeatId(),
                    es.getSeat().getRowNumber(),
                    es.getSeat().getSeatNumber(),
                    ("BOOKED".equals(es.getStatus()) || "HELD".equals(es.getStatus())) ? "booked" : "free",
                    toRowLetter(es.getSeat().getRowNumber()) + es.getSeat().getSeatNumber()
            );
        });
    }

    @Transactional
    public void reserveSeat(Long eventId, Long seatId) {
        var matches = eventSeatRepository.findByEvent_EventIdAndSeat_SeatIdIn(eventId, List.of(seatId));
        if (matches.isEmpty()) {
            throw new IllegalArgumentException("Seat not part of event");
        }
        EventSeat es = matches.get(0);
        if ("BOOKED".equals(es.getStatus()) || "HELD".equals(es.getStatus())) {
            throw new IllegalStateException("Seat already reserved");
        }
        es.setStatus("BOOKED");
        eventSeatRepository.save(es);
    }

    public void reserveSeatsBulk(Long eventId, SeatReservationRequest request) {
        List<Long> seatIds = request.getSeatIds();
        var seats = eventSeatRepository.findByEvent_EventIdAndSeat_SeatIdIn(eventId, seatIds);

        if (seats.size() != seatIds.size()) {
            throw new IllegalArgumentException("One or more seats are not part of the event");
        }

        for (EventSeat es : seats) {
            if ("BOOKED".equals(es.getStatus()) || "HELD".equals(es.getStatus())) {
                throw new IllegalStateException("One or more seats already reserved");
            }
            es.setStatus("BOOKED");
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
            if ("seating".equals(area.getType())) {
                // Check if seats already exist for this area
                if (!seatRepository.existsByArea_AreaId(area.getAreaId())) {
                    // Create seats for the area if they don't exist
                    int rows = getRowCountForArea(area.getName());
                    int cols = getSeatsPerRowForArea(area.getName());
                    setupSeats(area.getAreaId(), rows, cols);
                }

                // Create EventSeat records linking this event to all seats in the area
                List<Seat> seats = seatRepository.findByArea_AreaId(area.getAreaId());
                
                // Batch process EventSeat creation for better performance
                List<EventSeat> eventSeatsToCreate = new ArrayList<>();
                for (Seat seat : seats) {
                    // Check if EventSeat already exists
                    if (!eventSeatRepository.existsByEvent_EventIdAndSeat_SeatId(eventId, seat.getSeatId())) {
                        EventSeat eventSeat = new EventSeat();
                        eventSeat.setEvent(event);
                        eventSeat.setSeat(seat);
                        eventSeat.setStatus("FREE");
                        eventSeatsToCreate.add(eventSeat);
                    }
                }
                
                // Save all EventSeats in one batch operation
                if (!eventSeatsToCreate.isEmpty()) {
                    eventSeatRepository.saveAll(eventSeatsToCreate);
                }
            }
        }
    }

    private int getRowCountForArea(String areaName) {
        if (areaName.contains("Bag")) return 6;
        // NYT: Logic for Conference Hall
        if (areaName.contains("Conference Hall - Main Seating")) return 50; // 50 rækker
        return 2;
    }

    private int getSeatsPerRowForArea(String areaName) {
        if (areaName.contains("Bag")) return 15;
        // NYT: Logic for Conference Hall
        if (areaName.contains("Conference Hall - Main Seating")) return 30; // 30 sæder
        return 10;
    }

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
        // Generate seats using streams for clarity and maintainability
        List<Seat> seatsToCreate = IntStream.rangeClosed(1, rows)
                .boxed()
                .flatMap(r -> IntStream.rangeClosed(1, cols)
                        .mapToObj(c -> {
                            Seat seat = new Seat();
                            seat.setRowNumber(r);
                            seat.setSeatNumber(c);
                            seat.setArea(area);
                            return seat;
                        }))
                .collect(Collectors.toList());

        // Save all seats in one batch operation
        seatRepository.saveAll(seatsToCreate);
    }


    //Used for pricemodifier
    private boolean isBalcony(String areaName) {
        if (areaName == null) return false;
        String n = areaName.toLowerCase();
        return n.contains("balcony") || n.contains("balkon");
    }

    // I SeatService.java -> buildEventMap()

    public EventMapDto buildEventMap(Long eventId) {
        var event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found"));
        // Use optimized query with JOIN FETCH to avoid N+1 problem
        List<EventSeat> allEventSeats = eventSeatRepository.findByEvent_EventId(eventId);
        String clientSessionId = sessionService.generateUniqueSessionId();

        // Create parallel stream for better performance with large datasets
        Map<Long, List<EventSeat>> seatingSeatsByAreaId = allEventSeats.parallelStream()
                .filter(es -> es.getSeat() != null && es.getSeat().getArea() != null)
                .collect(Collectors.groupingByConcurrent(es -> es.getSeat().getArea().getAreaId()));

        // Pre-compute standing event seats to avoid repeated filtering
        List<EventSeat> standingEventSeats = allEventSeats.parallelStream()
                .filter(es -> es.getSeat() == null)
                .collect(Collectors.toList());

        List<Area> allAreasInHall = areaRepository.findByHall_HallId(event.getHall().getHallId());

        // Use parallel stream for area processing
        var areaMapDtos = allAreasInHall.parallelStream().map(area -> {
            int bookedCountForArea;
            Double priceForArea = event.getBasePrice() != null ? event.getBasePrice().doubleValue() : 0.0;
            List<SeatMapDto> seatDtosForArea;

            if ("standing".equals(area.getType())) {
                // Use pre-filtered standing seats
                bookedCountForArea = (int) standingEventSeats.stream()
                        .filter(es -> ("BOOKED".equals(es.getStatus()) || "HELD".equals(es.getStatus())))
                        .count();
                seatDtosForArea = List.of();
            } else { // Seating area
                // Use pre-grouped seats by area
                List<EventSeat> seatingEventSeats = seatingSeatsByAreaId.getOrDefault(area.getAreaId(), List.of());
                
                // Pre-compute booked count to avoid repeated iteration
                bookedCountForArea = (int) seatingEventSeats.stream()
                        .filter(es -> "BOOKED".equals(es.getStatus()) || "HELD".equals(es.getStatus()))
                        .count();

                // Optimize seat mapping with parallel stream
                seatDtosForArea = seatingEventSeats.parallelStream()
                        .filter(es -> es.getSeat() != null)
                        .map(es -> {
                            Seat s = es.getSeat();
                            String status;
                            if ("BOOKED".equals(es.getStatus())) {
                                status = "booked";
                            } else if ("HELD".equals(es.getStatus()) && clientSessionId.equals(es.getSessionId())) {
                                status = "held_by_me";
                            } else if ("HELD".equals(es.getStatus())) {
                                status = "held_by_other";
                            } else {
                                status = "free";
                            }
                            String label = toRowLetter(s.getRowNumber()) + s.getSeatNumber();
                            return new SeatMapDto(s.getSeatId(), s.getRowNumber(), s.getSeatNumber(), status, label, area.getAreaId(), priceForArea);
                        })
                        .sorted(java.util.Comparator.comparingInt(SeatMapDto::getRowNumber).thenComparingInt(SeatMapDto::getSeatNumber))
                        .toList();
            }

            return new AreaMapDto(area.getAreaId(), area.getName(), area.getType(), area.getCapacity(), bookedCountForArea, priceForArea, seatDtosForArea);
        }).toList();

        String hallName = event.getHall() != null ? event.getHall().getName() : "Ukendt Hal";
        if (hallName.isEmpty() && !areaMapDtos.isEmpty()) {
            hallName = areaMapDtos.get(0).getAreaName();
        }

        return new EventMapDto(event.getEventId(), event.getTitle(), hallName, areaMapDtos, event.getIsVisible());
    }

    // Holder sæde midlertidigt
    @Transactional
    public String holdSeats(Long eventId, List<Long> seatIds, String incomingSessionId) {
        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("Ingen sæder angivet for reservation.");
        }

        try {
            // Opret eller genbrug session ID. Hvis kunden sender et eksisterende ID, bruger vi det.
            // Ellers genererer vi et nyt. Dette er vigtigt for at kunden kan se sine egne holds.
            String currentSessionId = (incomingSessionId != null && !incomingSessionId.isEmpty()) ? incomingSessionId : sessionService.generateUniqueSessionId();

            // Før vi holder nye sæder, frigør vi alle sæder, som denne session ID allerede holder
            // for dette event. Dette forhindrer en kunde i at holde for mange sæder på tværs af forsøg.
            releaseSeatsBySessionId(eventId, currentSessionId);

            // Use pessimistic locking to prevent race conditions
            List<EventSeat> eventSeatsToHold = eventSeatRepository.findByEvent_EventIdAndSeat_SeatIdInWithLock(eventId, seatIds);

            if (eventSeatsToHold.size() != seatIds.size()) {
                throw new IllegalArgumentException("Et eller flere af de valgte sæder er ikke del af eventet eller eksisterer ikke.");
            }

            LocalDateTime heldUntil = LocalDateTime.now().plusMinutes(5); // Hold sæder i 5 minutter

            for (EventSeat es : eventSeatsToHold) {
                // Tjekker om sædet allerede er permanent booket
                if ("BOOKED".equals(es.getStatus())) {
                    throw new IllegalStateException("Sæde " + es.getSeat().getSeatNumber() + " (Række " + es.getSeat().getRowNumber() + ") er allerede permanent booket.");
                }
                // Tjekker om sædet allerede er holdt af en *anden* session og holdet ikke er udløbet
                if ("HELD".equals(es.getStatus())) {
                    if(!currentSessionId.equals(es.getSessionId())) {
                        if(es.getHeldUntil() != null && es.getHeldUntil().isAfter(LocalDateTime.now())) {
                            throw new IllegalStateException("Sæde" + es.getSeat().getSeatNumber() + " (Række " + es.getSeat().getRowNumber() + ") er midlertidigt holdt af en anden kunde");
                        }
                     }
                }

                es.setStatus("HELD");
                es.setHeldUntil(heldUntil);
                es.setSessionId(currentSessionId); // Gem den session, der holder sædet
            }
            eventSeatRepository.saveAll(eventSeatsToHold);
            return currentSessionId; // Returner den session ID, der nu holder sæderne
        } catch (LockTimeoutException | PessimisticLockException e) {
            // Handle lock acquisition failures
            throw new IllegalStateException("Kunne ikke reservere sæderne. Prøv venligst igen. Sæderne kan blive behandlet af en anden bruger.", e);
        }
    }

    // NY METODE: Frigiver sæder holdt af en specifik session
    @Transactional
    public void releaseSeatsBySessionId(Long eventId, String sessionId) {
        if (sessionId == null || sessionId.isEmpty()) return; // Sikkerhedscheck

        List<EventSeat> heldSeats = eventSeatRepository.findByEvent_EventIdAndStatusAndSessionId(eventId, "HELD", sessionId);
        for (EventSeat es : heldSeats) {
            es.setStatus("FREE");
            es.setHeldUntil(null);
            es.setSessionId(null);
        }
        eventSeatRepository.saveAll(heldSeats);
    }
}