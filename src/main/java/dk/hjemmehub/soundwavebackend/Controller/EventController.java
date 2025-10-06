package dk.hjemmehub.soundwavebackend.Controller;

import dk.hjemmehub.soundwavebackend.DTO.SeatDto;
import dk.hjemmehub.soundwavebackend.Service.SeatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dk.hjemmehub.soundwavebackend.Model.Event;
import dk.hjemmehub.soundwavebackend.Repository.EventRepository;
import dk.hjemmehub.soundwavebackend.Repository.HallRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
public class EventController {

    private final EventRepository eventRepository;
    private final HallRepository hallRepository;
    private final SeatService seatService;

    public EventController(EventRepository eventRepository, HallRepository hallRepository, SeatService seatService) {
        this.eventRepository = eventRepository;
        this.hallRepository = hallRepository;
        this.seatService = seatService;
    }

    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
    @GetMapping("/{id}")
    public Event getEventById(@PathVariable Long id) {
        return eventRepository.findById(id).orElseThrow();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Event createEvent(@RequestBody Event event) {
        Event savedEvent = eventRepository.save(event);
        // Automatically generate seats for the newly created event
        seatService.generateSeatsForEvent(savedEvent.getEventId());
        return savedEvent;
    }

    @PutMapping("/{id}")
    public Event updateEvent(@PathVariable Long id, @RequestBody Event event) {
        event.setEventId(id);
        return eventRepository.save(event);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable Long id) {
        eventRepository.deleteById(id);
    }

    // keep simple event endpoints here (no direct seat mutations)
    @GetMapping("/{eventId}/seats")
    public List<SeatDto> getSeatsByEvent(@PathVariable Long eventId) {
        return seatService.getSeatsForEvent(eventId);
    }

}
