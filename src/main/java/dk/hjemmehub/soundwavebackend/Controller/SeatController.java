package dk.hjemmehub.soundwavebackend.Controller;

import dk.hjemmehub.soundwavebackend.DTO.SeatHoldRequestDTO;
import dk.hjemmehub.soundwavebackend.DTO.ReservationRequestDto;
import dk.hjemmehub.soundwavebackend.Service.SeatService;
import dk.hjemmehub.soundwavebackend.Service.SessionService;
import dk.hjemmehub.soundwavebackend.DTO.SeatDto;
import dk.hjemmehub.soundwavebackend.DTO.EventMapDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/events/{eventId}/seats")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
public class SeatController {

    private final SeatService seatService;
    private final SessionService sessionService;

    public SeatController(SeatService seatService, SessionService sessionService) {
        this.seatService = seatService;
        this.sessionService = sessionService;
    }

    @PostMapping("/{seatId}/reserve")
    public ResponseEntity<?> reserveSeat(@PathVariable Long eventId, @PathVariable Long seatId) {
        seatService.reserveSeat(eventId, seatId);
        return ResponseEntity.ok().body("reserved");
    }

    @PostMapping("/reserve")
    public ResponseEntity<?> reserveSeatsBulk(@PathVariable Long eventId, @RequestBody ReservationRequestDto request) {
        seatService.reserveSeatsBulk(eventId, request);
        return ResponseEntity.ok().body("reserved-bulk");
    }

    @GetMapping("/map")
    public ResponseEntity<EventMapDto> getEventMap(@PathVariable Long eventId) {
        EventMapDto map = seatService.buildEventMap(eventId);
        return ResponseEntity.ok(map);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<SeatDto>> getSeatsForEventPaginated(
            @PathVariable Long eventId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        Page<SeatDto> seats = seatService.getSeatsForEventPaginated(eventId, page, size);
        return ResponseEntity.ok(seats);
    }

    @PostMapping("/generate-seats")
    public ResponseEntity<String> generateSeatsForEvent(@PathVariable Long eventId) {
        try {
           seatService.generateSeatsForEvent(eventId);
                return ResponseEntity.ok("Seats generated successfully for event " + eventId);
           } catch (Exception e) {
                return ResponseEntity.badRequest().body("Error generating seats: " + e.getMessage());
           }
    }

    @PostMapping("/hold")
    public ResponseEntity<String> holdSeats(@PathVariable Long eventId, @RequestBody SeatHoldRequestDTO request, HttpServletRequest httpRequest) {
        // Hent session ID fra request DTO'en.
        // Dette er den ID, som frontend har gemt (f.eks. i localStorage) og sender med.
        String currentSessionId = request.getSessionId();

        try {
            String newSessionId = seatService.holdSeats(eventId, request.getSeatIds(), currentSessionId);
            // Returner den session ID, der nu holder sæderne. Frontend skal gemme denne.
            return ResponseEntity.ok().body(newSessionId);
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Returner en fejlmeddelelse, hvis sæder ikke kan holdes
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/release-held-seats") // Ny endpoint
    public ResponseEntity<String> releaseHeldSeats(@PathVariable Long eventId, @RequestBody Map<String, String> requestBody) {
        String sessionId = requestBody.get("sessionId");
        if (sessionId == null || sessionId.isEmpty()) {
            return ResponseEntity.badRequest().body("Session ID mangler.");
        }
        try {
            seatService.releaseSeatsBySessionId(eventId, sessionId);
            return ResponseEntity.ok("Holdte sæder frigivet for session " + sessionId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fejl ved frigivelse af sæder: " + e.getMessage());
        }
    }
}


