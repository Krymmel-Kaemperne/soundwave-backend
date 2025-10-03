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
        if (hallRepository.count() > 0) {
            return;
        }

        // let the database generate IDs
        Hall hall1 = new Hall(null, "Main Concert Hall", 5000, "City Center");
        Hall hall2 = new Hall(null, "Small Club Stage", 500, "Downtown");

        hallRepository.save(hall1);
        hallRepository.save(hall2);

        Event rockConcert = new Event(
                "Red Hot Chili Peppers",
                "Gør dig klar til en aften med ren, uforfalsket funk-rock energi! De legendariske Red Hot Chili Peppers indtager scenen og leverer et show, der spænder over årtiers ikoniske hits. Fra de sjælfulde melodier i 'Under the Bridge' til den rå kraft i 'Give It Away' – oplev Fleas baslinjer, John Frusciantes guitar-magi og Anthony Kiedis' karismatiske vokal live. Dette er ikke bare en koncert; det er en historielektion i rock and roll.",
                BigDecimal.valueOf(500), "Scheduled",
                LocalDateTime.of(2025, 11, 1, 20, 0), hall1
        );
        rockConcert.setImageUrl("/images/Red-Hot-Chili-Peppers-Tour.webp");

        Event jazzNight = new Event(
                "Jazz Night: Armstrong's Resurrection",
                "I et musikalsk mirakel, der vil gå over i historien, vender Louis 'Satchmo' Armstrong tilbage fra det hinsides for én aften kun! Oplev den legendariske trompet og den umiskendelige, grusede stemme, der definerede jazzen for en hel verden. Fra 'What a Wonderful World' til 'La Vie en Rose' – lad dig rive med på en magisk rejse tilbage i tiden. Dette er mere end en hyldest; det er en genopstandelse. Gå ikke glip af chancen for at opleve en sand legende.",
                BigDecimal.valueOf(8000), "Sold Out",
                LocalDateTime.of(2025, 11, 5, 19, 30), hall1
        );
        jazzNight.setImageUrl("/images/LouisArmstrong.webp");


        Event comedyShow = new Event(
                "Rowan Atkinson: Mr. Bean vender tilbage!",
                "Han har været stille alt for længe, men nu er han tilbage! Den uforlignelige mester af fysisk komik, Rowan Atkinson, tager sit elskede alter ego, Mr. Bean, med på scenen for et helt nyt live-show. Forvent en aften med akavet stilhed, geniale løsninger på ikke-eksisterende problemer og selvfølgelig den gule bil og Teddy. Atkinson vil også dykke ned i sine andre ikoniske roller og præsentere helt nyt materiale. Gør dig klar til at græde af grin!",
                BigDecimal.valueOf(50),
                "Scheduled",
                LocalDateTime.of(2025, 11, 3, 21, 0),
                hall2
        );
        comedyShow.setImageUrl("/images/RowanAtkinson.webp");


        Event indieBand = new Event(
                "Radiohead: En intim aften",
                "Glem de udsolgte stadionkoncerter! I en sjælden og eksklusiv optræden giver pionererne inden for alternativ rock, Radiohead, en intim koncert på vores Small Club Stage. Oplev de komplekse lydlandskaber, de hjerteskærende melodier og den soniske dybde fra deres legendariske bagkatalog i et helt unikt, nært format. Fra 'Creep' til 'Karma Police' og deres mere eksperimenterende værker – dette er en enestående chance for at opleve et af verdens største bands helt tæt på. Billetter er strengt begrænsede!",
                BigDecimal.valueOf(260),
                "Scheduled",
                LocalDateTime.of(2025, 11, 10, 20, 30),
                hall2
        );
        indieBand.setImageUrl("/images/RadioHead.webp");


        eventRepository.saveAll(List.of(rockConcert, jazzNight, comedyShow, indieBand));
    }
}