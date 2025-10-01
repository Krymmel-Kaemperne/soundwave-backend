package dk.hjemmehub.soundwavebackend.Config;

import dk.hjemmehub.soundwavebackend.Model.Event;
import dk.hjemmehub.soundwavebackend.Model.Hall;
import dk.hjemmehub.soundwavebackend.Repository.EventRepository;
import dk.hjemmehub.soundwavebackend.Repository.HallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class InitData implements CommandLineRunner {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private HallRepository hallRepository;

    @Override
    public void run(String... args) throws Exception {

        Hall hall1 = new Hall(1, "Main Concert Hall", 5000, "City Center");
        Hall hall2 = new Hall(2, "Small Club Stage", 500, "Downtown");

        hallRepository.save(hall1);
        hallRepository.save(hall2);


        Event rockConcert = new Event(
                "Rock Concert",
                "Live rock music",
                BigDecimal.valueOf(100),
                "Scheduled",
                LocalDateTime.of(2025, 11, 1, 20, 0),
                hall1
        );

        Event jazzNight = new Event(
                "Jazz Night",
                "Evening of smooth jazz",
                BigDecimal.valueOf(80),
                "Scheduled",
                LocalDateTime.of(2025, 11, 5, 19, 30),
                hall1
        );

        Event comedyShow = new Event(
                "Comedy Show",
                "Stand-up comedy special",
                BigDecimal.valueOf(50),
                "Scheduled",
                LocalDateTime.of(2025, 11, 3, 21, 0),
                hall2
        );

        Event indieBand = new Event(
                "Indie Band Performance",
                "Up-and-coming local indie band",
                BigDecimal.valueOf(500),
                "Scheduled",
                LocalDateTime.of(2025, 11, 10, 20, 30),
                hall2
        );

        eventRepository.saveAll(List.of(rockConcert, jazzNight, comedyShow, indieBand));
    }
}
