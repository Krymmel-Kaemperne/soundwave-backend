package dk.hjemmehub.soundwavebackend.Config;

import dk.hjemmehub.soundwavebackend.Model.Area;
import dk.hjemmehub.soundwavebackend.Model.Event;
import dk.hjemmehub.soundwavebackend.Model.EventSeat;
import dk.hjemmehub.soundwavebackend.Model.Hall;
import dk.hjemmehub.soundwavebackend.Model.Seat;
import dk.hjemmehub.soundwavebackend.Repository.AreaRepository;
import dk.hjemmehub.soundwavebackend.Repository.EventRepository;
import dk.hjemmehub.soundwavebackend.Repository.EventSeatRepository;
import dk.hjemmehub.soundwavebackend.Repository.HallRepository;
import dk.hjemmehub.soundwavebackend.Repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class InitData implements CommandLineRunner {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private HallRepository hallRepository;
    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private EventSeatRepository eventSeatRepository;

    @Override
    public void run(String... args) throws Exception {
        // Kontrollerer om der allerede er data for at undgå duplikater ved hver genstart
        if (eventRepository.count() > 0) {
            System.out.println("Database allerede fyldt med eventdata. Skipper initialisering.");
            return;
        }

        System.out.println("Initialiserer database med data...");

        // 1. Opret Sale (Halls)
        Hall hall1 = new Hall(null, "Koncert Arena", 5000, "City Center");
        Hall hall2 = new Hall(null, "Konference Sal", 500, "City Center");
        hallRepository.saveAll(List.of(hall1, hall2));

        // 2. Opret Events
        Event rockConcert = new Event(
                "Red Hot Chili Peppers",
                "Gør dig klar til en aften med ren, uforfalsket funk-rock energi! De legendariske Red Hot Chili Peppers indtager scenen og leverer et show, der spænder over årtiers ikoniske hits. Fra de sjælfulde melodier i 'Under the Bridge' til den rå kraft i 'Give It Away' – oplev Fleas baslinjer, John Frusciantes guitar-magi og Anthony Kiedis' karismatiske vokal live. Dette er ikke bare en koncert; det er en historielektion i rock and roll.",
                BigDecimal.valueOf(500), "Scheduled",
                LocalDateTime.of(2025,  11, 1, 20, 0), hall1
        );
        rockConcert.setImageUrl("/images/Red-Hot-Chili-Peppers-Tour.webp");
        rockConcert.setIsVisible(true);

        Event jazzNight = new Event(
                "Jazz Night: Armstrong's Resurrection",
                "I et musikalsk mirakel, der vil gå over i historien, vender Louis 'Satchmo' Armstrong tilbage fra det hinsides for én aften kun! Oplev den legendariske trompet og den umiskendelige, grusede stemme, der definerede jazzen for en hel verden. Fra 'What a Wonderful World' til 'La Vie en Rose' – lad dig rive med på en magisk rejse tilbage i tiden. Dette er mere end en hyldest; det er en genopstandelse. Gå ikke glip af chancen for at opleve en sand legende.",
                BigDecimal.valueOf(800), "Sold Out",
                LocalDateTime.of(2025, 11, 5, 19, 30), hall1
        );
        jazzNight.setImageUrl("/images/LouisArmstrong.webp");
        jazzNight.setIsVisible(true);

        Event comedyShow = new Event(
                "Rowan Atkinson: Mr. Bean vender tilbage!",
                "Han har været stille alt for længe, men nu er han tilbage! Den uforlignelige mester af fysisk komik, Rowan Atkinson, tager sit elskede alter ego, Mr. Bean, med på scenen for et helt nyt live-show. Forvent en aften med akavet stilhed, geniale løsninger på ikke-eksisterende problemer og selvfølgelig den gule bil og Teddy. Atkinson vil også dykke ned i sine andre ikoniske roller og præsentere helt nyt materiale. Gør dig klar til at græde af grin!",
                BigDecimal.valueOf(50),
                "Scheduled",
                LocalDateTime.of(2025, 11, 3, 21, 0),
                hall2
        );
        comedyShow.setImageUrl("/images/RowanAtkinson.webp");
        comedyShow.setIsVisible(true);

        Event indieBand = new Event(
                "Radiohead: En intim aften",
                "Glem de udsolgte stadionkoncerter! I en sjælden og eksklusiv optræden giver pionererne inden for alternativ rock, Radiohead, en intim koncert på vores Small Club Stage. Oplev de komplekse lydlandskaber, de hjerteskærende melodier og den soniske dybde fra deres legendariske bagkatalog i et helt unikt, nært format. Fra 'Creep' til 'Karma Police' og deres mere eksperimenterende værker – dette er en enestående chance for at opleve et af verdens største bands helt tæt på. Billetter er strengt begrænsede!",
                BigDecimal.valueOf(260),
                "Scheduled",
                LocalDateTime.of(2025, 11, 10, 20, 30),
                hall2
        );
        indieBand.setImageUrl("/images/RadioHead.webp");
        indieBand.setIsVisible(true);

        eventRepository.saveAll(List.of(rockConcert, jazzNight, comedyShow, indieBand));

        // 3. Opret Areas for Hall 1 (Koncert Arena)
        Area standingFloor = new Area();
        standingFloor.setName("Gulvet (Ståpladser)");
        standingFloor.setType("standing");
        standingFloor.setCapacity(1000);
        standingFloor.setHall(hall1);
        areaRepository.save(standingFloor);

        Area vipLeft = new Area();
        vipLeft.setName("VIP Balkon - Venstre (Siddepladser)");
        vipLeft.setType("seating");
        vipLeft.setHall(hall1);
        areaRepository.save(vipLeft);

        Area vipRight = new Area();
        vipRight.setName("VIP Balkon - Højre (Siddepladser)");
        vipRight.setType("seating");
        vipRight.setHall(hall1);
        areaRepository.save(vipRight);

        Area vipBack = new Area();
        vipBack.setName("VIP Balkon - Bag (Siddepladser)");
        vipBack.setType("seating");
        vipBack.setHall(hall1);
        areaRepository.save(vipBack);

        // 4. Forbered liste til EventSeats
        List<EventSeat> allEventSeatsToSave = new ArrayList<>();

        // VIP Balkon Venstre (Koncert Arena):
        allEventSeatsToSave.addAll(createAndAssignSeats(vipLeft, 2, 10, rockConcert, 0.3));

        // VIP Balkon Højre (Koncert Arena):
        allEventSeatsToSave.addAll(createAndAssignSeats(vipRight, 2, 10, rockConcert, 0.3));

        // VIP Balkon Bag (Koncert Arena):
        allEventSeatsToSave.addAll(createAndAssignSeats(vipBack, 6, 15, rockConcert, 0.4));

        // 5. Simuler ståpladser for Koncert Arena (for rockConcert)
        int totalStandingCapacity = standingFloor.getCapacity();
        int bookedStandingCount = 750;
        int availableStanding = totalStandingCapacity - bookedStandingCount;

        for (int i = 0; i < bookedStandingCount; i++) {
            EventSeat bookedStandingSeat = new EventSeat();
            bookedStandingSeat.setEvent(rockConcert);
            bookedStandingSeat.setSeat(null);
            bookedStandingSeat.setArea(standingFloor);
            bookedStandingSeat.setStatus("BOOKED");
            //...
            allEventSeatsToSave.add(bookedStandingSeat);
        }

        for (int i = 0; i < availableStanding; i++) {
            EventSeat freeStandingSeat = new EventSeat();
            freeStandingSeat.setEvent(rockConcert);
            freeStandingSeat.setSeat(null);
            freeStandingSeat.setArea(standingFloor);
            freeStandingSeat.setStatus("FREE");
            //...
            allEventSeatsToSave.add(freeStandingSeat);
        }
        System.out.println("Ståpladser for Rock Concert: " + bookedStandingCount + " bookede, " + availableStanding + " ledige");

        // 6. Setup Konference Salen seating layout
        Area conferenceMain = new Area();
        conferenceMain.setName("Conference Hall - Main Seating");
        conferenceMain.setType("seating");
        conferenceMain.setHall(hall2);
        areaRepository.save(conferenceMain);

        // Create 50 rows x 30 seats for Conference Hall
        List<Seat> conferenceSeats = new ArrayList<>();
        for (int r = 1; r <= 50; r++) {
            for (int c = 1; c <= 30; c++) {
                Seat seat = new Seat();
                seat.setRowNumber(r);
                seat.setSeatNumber(c);
                seat.setArea(conferenceMain);
                conferenceSeats.add(seat);
            }
        }
        seatRepository.saveAll(conferenceSeats);
        conferenceMain.setSeats(conferenceSeats);

        // Assign EventSeats to all events in Hall 2
        List<Event> eventsInConference = List.of(comedyShow, indieBand);
        for (Event event : eventsInConference) {
            for (Seat seat : conferenceSeats) {
                EventSeat es = new EventSeat();
                es.setEvent(event);
                es.setSeat(seat);
                if (Math.random() < 0.25) {
                    es.setStatus("BOOKED");
                } else {
                    es.setStatus("FREE");
                }
                es.setHeldUntil(null);
                es.setSessionId(null);
                allEventSeatsToSave.add(es);
            }
        }

        // Gem ALLE EventSeats samlet, helt til sidst i run() metoden!
        eventSeatRepository.saveAll(allEventSeatsToSave);
        System.out.println("Database initialisering fuldført (alle EventSeats gemt).");
    }

    // Denne metode er en hjælperfunktion og skal ligge inden for InitData klassen.
    private List<EventSeat> createAndAssignSeats(Area area, int rows, int cols, Event event, double bookingProbability) {
        List<Seat> seats = new ArrayList<>();
        List<EventSeat> eventSeats = new ArrayList<>();

        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= cols; c++) {
                Seat seat = new Seat();
                seat.setRowNumber(r);
                seat.setSeatNumber(c);
                seat.setArea(area);
                seats.add(seat);
            }
        }
        seatRepository.saveAll(seats);

        Random random = new Random();

        for (Seat seat : seats) {
            EventSeat es = new EventSeat();
            es.setEvent(event);
            es.setSeat(seat);
            es.setArea(area); // <-- TILFØJ DENNE LINJE
            if (random.nextDouble() < bookingProbability) {
                es.setStatus("BOOKED");
            } else {
                es.setStatus("FREE");
            }
            es.setHeldUntil(null);
            es.setSessionId(null);
            eventSeats.add(es);
        }
        return eventSeats;
    }
}