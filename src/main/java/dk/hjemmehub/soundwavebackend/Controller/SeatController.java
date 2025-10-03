package dk.hjemmehub.soundwavebackend.Controller;

import dk.hjemmehub.soundwavebackend.DTO.SeatReservationRequest;
import dk.hjemmehub.soundwavebackend.Service.SeatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dk.hjemmehub.soundwavebackend.DTO.EventMapDto;

@RestController
@RequestMapping("/events/{eventId}/seats")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @PostMapping("/{seatId}/reserve")
    public ResponseEntity<?> reserveSeat(@PathVariable Long eventId, @PathVariable Long seatId) {
        seatService.reserveSeat(eventId, seatId);
        return ResponseEntity.ok().body("reserved");
    }

    @PostMapping("/reserve")
    public ResponseEntity<?> reserveSeatsBulk(@PathVariable Long eventId, @RequestBody SeatReservationRequest request) {
        seatService.reserveSeatsBulk(eventId, request);
        return ResponseEntity.ok().body("reserved-bulk");
    }

    @GetMapping("/map")
    public ResponseEntity<EventMapDto> getEventMap(@PathVariable Long eventId) {
        EventMapDto map = seatService.buildEventMap(eventId);
        return ResponseEntity.ok(map);
    }

}


