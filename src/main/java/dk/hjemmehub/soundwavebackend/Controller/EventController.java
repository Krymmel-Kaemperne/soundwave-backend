package dk.hjemmehub.soundwavebackend.Controller;

import dk.hjemmehub.soundwavebackend.Model.Event;
import dk.hjemmehub.soundwavebackend.Repository.EventRepository;
import dk.hjemmehub.soundwavebackend.Repository.HallRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class EventController {

    private final EventRepository eventRepository;
    private final HallRepository hallRepository;

    public EventController(EventRepository eventRepository, HallRepository hallRepository) {
        this.eventRepository = eventRepository;
        this.hallRepository = hallRepository;
    }

    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
    @GetMapping("/{id}")
    public Event getEventById(@PathVariable int id) {
        return eventRepository.findById(id).orElseThrow();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Event createEvent(@RequestBody Event event) {
        return eventRepository.save(event);
    }

    @PutMapping("/{id}")
    public Event updateEvent(@PathVariable int id, @RequestBody Event event) {
        event.setEventId(id);
        return eventRepository.save(event);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable int id) {
        eventRepository.deleteById(id);
    }

}