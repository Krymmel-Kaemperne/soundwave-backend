package dk.hjemmehub.soundwavebackend.Controller;

import dk.hjemmehub.soundwavebackend.DTO.SeatReservationRequest;
import dk.hjemmehub.soundwavebackend.Service.SeatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import dk.hjemmehub.soundwavebackend.DTO.EventMapDto;

@RestController
@RequestMapping("/events/{eventId}/seats")
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


