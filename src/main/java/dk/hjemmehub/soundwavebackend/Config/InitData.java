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
        if (eventRepository.count() > 0) { // Bruger eventRepository for at tjekke
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
                LocalDateTime.of(2025, 11, 1, 20, 0), hall1
        );
        rockConcert.setImageUrl("/images/Red-Hot-Chili-Peppers-Tour.webp");

        Event jazzNight = new Event(
                "Jazz Night: Armstrong's Resurrection",
                "I et musikalsk mirakel, der vil gå over i historien, vender Louis 'Satchmo' Armstrong tilbage fra det hinsides for én aften kun! Oplev den legendariske trompet og den umiskendelige, grusede stemme, der definerede jazzen for en hel verden. Fra 'What a Wonderful World' til 'La Vie en Rose' – lad dig rive med på en magisk rejse tilbage i tiden. Dette er mere end en hyldest; det er en genopstandelse. Gå ikke glip af chancen for at opleve en sand legende.",
                BigDecimal.valueOf(800), "Sold Out", // Rettelser til basePrice
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

        // 3. Opret Areas for Hall 1 (The Main Arena) - Disse matcher din frontend mock-data
        Area standingFloor = new Area();
        standingFloor.setName("Gulvet (Ståpladser)");
        standingFloor.setType("standing");
        standingFloor.setCapacity(1000); // F.eks. 1000 ståpladser
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

        // 4. Opret Sæder for Seating Areas (VipLeft, VipRight, VipBack) og tilknyt dem til Rock Concert
        // Tilføj alle EventSeats til en samlet liste
        List<EventSeat> allEventSeatsToSave = new ArrayList<>();

        // VIP Balkon Venstre: 2 rækker, 10 sæder, book 2 i hver række
        allEventSeatsToSave.addAll(createAndAssignSeats(vipLeft, 2, 10, rockConcert, 2));

        // VIP Balkon Højre: 2 rækker, 10 sæder, book 2 i hver række
        allEventSeatsToSave.addAll(createAndAssignSeats(vipRight, 2, 10, rockConcert, 2));

        // VIP Balkon Bag: 6 rækker, 15 sæder, book 15 i hver række (dvs. alle)
        allEventSeatsToSave.addAll(createAndAssignSeats(vipBack, 6, 15, rockConcert, 5));

        // 5. Opret EventSeat for Standing Area (simulerer antal bookinger)
        // Opretter ét EventSeat, selvom det er en standing area, for at den dukker op i EventMapDto
        EventSeat standingEventSeat = new EventSeat();
        standingEventSeat.setEvent(rockConcert);
        standingEventSeat.setSeat(null); // VIGTIGT: Ingen specifikt Seat-objekt for ståpladser
        // Simulerer bookede ståpladser: 750 booket ud af 1000 capacity = 250 ledige.
        standingEventSeat.setReserved(true); // Sætter den til 'reserved' for at den tæller med i bookedCount.
        // Håndtering af kapacitet/bookedCount/available for standing areas sker primært i SeatService.
        allEventSeatsToSave.add(standingEventSeat);

        // Gem alle EventSeats samlet
        eventSeatRepository.saveAll(allEventSeatsToSave);

        System.out.println("Database initialisering fuldført.");
    }

    // NY HJÆLPEMETODE: Returnerer listen af EventSeats


    private List<EventSeat> createAndAssignSeats(Area area, int rows, int cols, Event event, int bookedSeatsPerRow) {
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
        seatRepository.saveAll(seats); // Gem alle sæder først for at få ID'er

        java.util.Random random = new java.util.Random();
        double bookingProbability = 0.3;

        for (Seat seat : seats) {
            EventSeat eventSeat = new EventSeat();
            eventSeat.setEvent(event);
            eventSeat.setSeat(seat);

            // Randomiser booking: Tilfældigt tal mellem 0.0 og 1.0. Hvis det er mindre end bookingProbability, book sædet.
            eventSeat.setReserved(random.nextDouble() < bookingProbability);

            eventSeats.add(eventSeat);
        }
        // returner eventSeats her, de gemmes samlet i run()
        return eventSeats;
    }
}