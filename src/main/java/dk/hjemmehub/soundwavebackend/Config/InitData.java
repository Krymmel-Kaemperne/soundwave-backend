package dk.hjemmehub.soundwavebackend.Config;

import dk.hjemmehub.soundwavebackend.Model.Event;
import dk.hjemmehub.soundwavebackend.Model.Hall;
import dk.hjemmehub.soundwavebackend.Repository.EventRepository;
import dk.hjemmehub.soundwavebackend.Repository.HallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
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
        if (hallRepository.count() > 0) {
            return;
        }

        Hall hall1 = new Hall(1, "Main Concert Hall", 5000, "City Center");
        Hall hall2 = new Hall(2, "Small Club Stage", 500, "Downtown");

        hallRepository.save(hall1);
        hallRepository.save(hall2);

        Event rockConcert = new Event(
                "Rock Concert", "Live rock music",
                BigDecimal.valueOf(100), "Scheduled",
                LocalDateTime.of(2025, 11, 1, 20, 0), hall1
        );
        // Sæt stien til billedet
        rockConcert.setImageUrl("/images/Red-Hot-Chili-Peppers-Tour.webp"); //

        Event jazzNight = new Event(
                "Jazz Night", "Evening of smooth jazz",
                BigDecimal.valueOf(80), "Sold Out",
                LocalDateTime.of(2025, 11, 5, 19, 30), hall1
        );
        jazzNight.setImageUrl("/images/jazz-night.jpg"); // Eksempel med en anden fil


        Event comedyShow = new Event(
                "Comedy Show",
                "Stand-up comedy special",
                BigDecimal.valueOf(50),
                "Scheduled",
                LocalDateTime.of(2025, 11, 3, 21, 0),
                hall2
        );
        comedyShow.setImageUrl("/images/comedy-show.jpg");


        Event indieBand = new Event(
                "Indie Band Performance",
                "Up-and-coming local indie band",
                BigDecimal.valueOf(60), // Juster prisen
                "Scheduled",
                LocalDateTime.of(2025, 11, 10, 20, 30),
                hall2
        );
        indieBand.setImageUrl("/images/indie-band.jpg");


        eventRepository.saveAll(List.of(rockConcert, jazzNight, comedyShow, indieBand));
    }
}