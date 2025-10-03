package dk.hjemmehub.soundwavebackend.Controller;

import dk.hjemmehub.soundwavebackend.DTO.SeatDto;
import dk.hjemmehub.soundwavebackend.Service.SeatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final SeatService seatService;

    public EventController(SeatService seatService) {
        this.seatService = seatService;
    }

    //FAKE GET METODEN (Slet "Fake" til sidst, så er det rigtigt)
    @GetMapping("/{eventId}/seats")
    public List<SeatDto> getSeatsByEvent(@PathVariable Long eventId) {
        return seatService.getSeatsForEvent(eventId);
    }
}
