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
        // Eksisterende Events
        Event rockConcert = new Event(
                "Red Hot Chili Peppers",
                "Gør dig klar til en aften med ren, uforfalsket funk-rock energi! De legendariske Red Hot Chili Peppers indtager scenen og leverer et show, der spænder over årtiers ikoniske hits. Fra de sjælfulde melodier i 'Under the Bridge' til den rå kraft i 'Give It Away' – oplev Fleas baslinjer, John Frusciantes guitar-magi og Anthony Kiedis' karismatiske vokal live. Dette er ikke bare en koncert; det er en historielektion i rock and roll.",
                BigDecimal.valueOf(500), "Scheduled",
                LocalDateTime.of(2025, 11, 1, 20, 0), hall1
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

        // --- NYE EVENTS STARTER HER ---
        Event tupacConcert = new Event(
                "2Pac: Hologram Resurrection",
                "En enestående chance for at opleve legenden 2Pac 'live' igen, i en banebrydende hologramkoncert fyldt med hans største hits. En aften, der vil skrive historie!",
                BigDecimal.valueOf(750), "Scheduled",
                LocalDateTime.of(2025, 11, 15, 20, 0), hall1
        );
        tupacConcert.setImageUrl("/images/2pac.webp");
        tupacConcert.setIsVisible(true);

        Event mileyCyrusConcert = new Event(
                "Miley Cyrus: Endless Summer Vacation Live",
                "Miley Cyrus leverer en uforglemmelig aften med hendes kraftfulde vokal og ikoniske hits fra 'Flowers' til 'Wrecking Ball'. Gør dig klar til et eksplosivt show!",
                BigDecimal.valueOf(600), "Scheduled",
                LocalDateTime.of(2025, 11, 18, 19, 30), hall1
        );
        mileyCyrusConcert.setImageUrl("/images/MileyCyrus.webp");
        mileyCyrusConcert.setIsVisible(true);

        Event ørkenensSønner = new Event(
                "Ørkenens Sønner: Tørst",
                "Tag med på en tør og humoristisk rejse gennem ørkenen med Ørkenens Sønner, som garanterer en aften fyldt med skøre indfald og musikalske perler.",
                BigDecimal.valueOf(350), "Scheduled",
                LocalDateTime.of(2025, 11, 20, 20, 0), hall2
        );
        ørkenensSønner.setImageUrl("/images/ØrkenensSønner.webp");
        ørkenensSønner.setIsVisible(true);

        Event stormzyConcert = new Event(
                "Stormzy: Heavy Is The Head Tour",
                "Den britiske grime-konge Stormzy indtager scenen med sin energiske performance og tekster, der rykker. En aften med rå kraft og uforglemmelig musik.",
                BigDecimal.valueOf(450), "Scheduled",
                LocalDateTime.of(2025, 11, 22, 20, 0), hall1
        );
        stormzyConcert.setImageUrl("/images/Stormzy.webp");
        stormzyConcert.setIsVisible(true);

        Event obamaForedrag = new Event(
                "Barack Obama: Yes We Can! - Foredrag om Lederskab",
                "En sjælden mulighed for at opleve den tidligere præsident Barack Obama dele sin indsigt i lederskab, forandring og fremtiden. En inspirerende aften.",
                BigDecimal.valueOf(1200), "Scheduled",
                LocalDateTime.of(2025, 11, 25, 19, 0), hall2
        );
        obamaForedrag.setImageUrl("/images/BarackObama.webp");
        obamaForedrag.setIsVisible(true);

        Event aminJensen = new Event(
                "Amin Jensen: Hit med sangen",
                "Amin Jensen inviterer til en aften fyldt med musikalitet, parodier og masser af latter. Gæt med på de kendte sange og syng med!",
                BigDecimal.valueOf(280), "Scheduled",
                LocalDateTime.of(2025, 11, 28, 20, 0), hall1
        );
        aminJensen.setImageUrl("/images/AminJensen.webp");
        aminJensen.setIsVisible(true);

        Event jarlTuxenForedrag = new Event(
                "Jarl Tuxen: JPA og API foredrag",
                "Dyk ned i den fascinerende verden af Java Persistence API og RESTful API design med Jarl Tuxen. For udviklere og tech-entusiaster.",
                BigDecimal.valueOf(150), "Scheduled",
                LocalDateTime.of(2025, 12, 1, 17, 0), hall2
        );
        jarlTuxenForedrag.setImageUrl("/images/JarlTuxen.webp"); // Placeholder
        jarlTuxenForedrag.setIsVisible(true);

        Event jaronKahalaniForedrag = new Event(
                "Jaron Kahalani: 16 Personalities - Forstå dig selv og andre",
                "Jaron Kahalani præsenterer en dybdegående analyse af de 16 personlighedstyper. Lær at forstå dine styrker og forbedre dine relationer.",
                BigDecimal.valueOf(200), "Scheduled",
                LocalDateTime.of(2025, 12, 3, 18, 0), hall2
        );
        jaronKahalaniForedrag.setImageUrl("/images/JaronKahalani.webp"); // Placeholder
        jaronKahalaniForedrag.setIsVisible(true);

        Event simonShineForedrag = new Event(
                "Simon Shine: Docker on Crack - En dybdegang i containere",
                "Simon Shine tager dig med på en intens rejse ind i Dockers verden. Lær de avancerede tricks og optimeringer i dette foredrag for den erfarne udvikler.",
                BigDecimal.valueOf(220), "Scheduled",
                LocalDateTime.of(2025, 12, 5, 17, 30), hall2
        );
        simonShineForedrag.setImageUrl("/images/SimonShine.webp"); // Placeholder
        simonShineForedrag.setIsVisible(true);

        Event popsiKrelle = new Event(
                "Popsi og Krelle: Musikalsk Legestue",
                "En magisk og interaktiv musikoplevelse for de yngste. Popsi og Krelle inviterer til sang, dans og leg i Koncert Arena.",
                BigDecimal.valueOf(120), "Scheduled",
                LocalDateTime.of(2025, 12, 8, 14, 0), hall1
        );
        popsiKrelle.setImageUrl("/images/PopsiOgKrelle.webp"); // Placeholder
        popsiKrelle.setIsVisible(true);

        Event rollingStones = new Event(
                "Rolling Stones: Hackney Diamonds Tour",
                "De legendariske Rolling Stones er tilbage! Oplev rock 'n' rollens ikoner live i Koncert Arena med hits fra deres seneste album og klassikere.",
                BigDecimal.valueOf(950), "Scheduled",
                LocalDateTime.of(2025, 12, 12, 20, 0), hall1
        );
        rollingStones.setImageUrl("/images/RollingStones.webp");
        rollingStones.setIsVisible(true);

        Event gorillazConcert = new Event(
                "Gorillaz: The World of Gorillaz Live",
                "Damon Albarn og hans virtuelle band Gorillaz leverer et visuelt spektakulært og musikalsk unikt show med alle deres anerkendte hits.",
                BigDecimal.valueOf(550), "Scheduled",
                LocalDateTime.of(2025, 12, 15, 20, 30), hall1
        );
        gorillazConcert.setImageUrl("/images/Gorillaz.webp");
        gorillazConcert.setIsVisible(true);

        Event frankSinatra = new Event(
                "Frank Sinatra: En magisk aften (Hologram)",
                "Tag på en tidsrejse til the golden age of jazz med et spektakulært hologramshow med den uforlignelige Frank Sinatra. What a wonderful night!",
                BigDecimal.valueOf(850), "Scheduled",
                LocalDateTime.of(2025, 12, 18, 19, 0), hall1
        );
        frankSinatra.setImageUrl("/images/FrankSinatra.webp"); // Placeholder
        frankSinatra.setIsVisible(true);

        Event taylorSwift = new Event(
                "Taylor Swift: The Eras Tour - Extended",
                "Taylor Swift bringer sin fænomenale Eras Tour til Soundwave Arena med et udvidet show. Gør dig klar til en aften med alle dine yndlingssange!",
                BigDecimal.valueOf(1100), "Scheduled",
                LocalDateTime.of(2025, 12, 22, 19, 0), hall1
        );
        taylorSwift.setImageUrl("/images/TaylorSwift.webp"); // Placeholder
        taylorSwift.setIsVisible(true);

        // Gem alle events
        eventRepository.saveAll(List.of(
                rockConcert, jazzNight, comedyShow, indieBand,
                tupacConcert, mileyCyrusConcert, ørkenensSønner, stormzyConcert,
                obamaForedrag, aminJensen, jarlTuxenForedrag, jaronKahalaniForedrag,
                simonShineForedrag, popsiKrelle, rollingStones, gorillazConcert,
                frankSinatra, taylorSwift
        ));

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

        // --- TILFØJ ALLE EVENTS TIL STÅPLADSOMRÅDET ---
        // Generer nu ståpladser for *alle* events i Hall 1
        List<Event> eventsInHall1 = List.of(rockConcert, jazzNight, tupacConcert, mileyCyrusConcert,
                stormzyConcert, aminJensen, popsiKrelle, rollingStones, gorillazConcert,
                frankSinatra, taylorSwift);

        int initialBookedStanding = 750; // Initialt bookede for hvert event (simulering)

        for (Event event : eventsInHall1) {
            int currentBookedCount = new Random().nextInt(initialBookedStanding - 100) + 100; // Random mellem 100 og 750
            int currentAvailableStanding = standingFloor.getCapacity() - currentBookedCount;

            for (int i = 0; i < currentBookedCount; i++) {
                EventSeat bookedStandingSeat = new EventSeat();
                bookedStandingSeat.setEvent(event);
                bookedStandingSeat.setSeat(null);
                bookedStandingSeat.setArea(standingFloor);
                bookedStandingSeat.setStatus("BOOKED");
                allEventSeatsToSave.add(bookedStandingSeat);
            }

            for (int i = 0; i < currentAvailableStanding; i++) {
                EventSeat freeStandingSeat = new EventSeat();
                freeStandingSeat.setEvent(event);
                freeStandingSeat.setSeat(null);
                freeStandingSeat.setArea(standingFloor);
                freeStandingSeat.setStatus("FREE");
                allEventSeatsToSave.add(freeStandingSeat);
            }
            System.out.println("Ståpladser for " + event.getTitle() + ": " + currentBookedCount + " bookede, " + currentAvailableStanding + " ledige");
        }


        // VIP Balkon Venstre (Koncert Arena):
        // Disse skal genereres for alle relevante events.
        List<Event> eventsWithVipSeating = List.of(
                rockConcert, jazzNight, tupacConcert, mileyCyrusConcert,
                stormzyConcert, aminJensen, popsiKrelle, rollingStones, gorillazConcert,
                frankSinatra, taylorSwift // Alle events i Hall 1, der skal have VIP-sæder
        );
        for (Event event : eventsWithVipSeating) {
            double prob = new Random().nextDouble() * 0.4 + 0.1; // Random booking mellem 10-50%
            allEventSeatsToSave.addAll(createAndAssignSeats(vipLeft, 2, 10, event, prob));
            allEventSeatsToSave.addAll(createAndAssignSeats(vipRight, 2, 10, event, prob));
            allEventSeatsToSave.addAll(createAndAssignSeats(vipBack, 6, 15, event, prob));
        }


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
        List<Event> eventsInConferenceHall = List.of(comedyShow, indieBand, ørkenensSønner, obamaForedrag,
                jarlTuxenForedrag, jaronKahalaniForedrag, simonShineForedrag); // ALLE i Hall 2
        for (Event event : eventsInConferenceHall) {
            for (Seat seat : conferenceSeats) {
                EventSeat es = new EventSeat();
                es.setEvent(event);
                es.setSeat(seat);
                es.setArea(conferenceMain); // Sæt area for Conference Hall sæder
                if (Math.random() < 0.25) { // 25% chance for at være booket
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
        // Først, find eller opret sæderne for dette specifikke område
        List<Seat> seats = seatRepository.findByArea_AreaId(area.getAreaId());
        if (seats.isEmpty()) { // Hvis sæderne ikke findes endnu, opret dem
            for (int r = 1; r <= rows; r++) {
                for (int c = 1; c <= cols; c++) {
                    Seat seat = new Seat();
                    seat.setRowNumber(r);
                    seat.setSeatNumber(c);
                    seat.setArea(area);
                    seats.add(seat);
                }
            }
            seatRepository.saveAll(seats); // Gem de nye sæder
        }

        List<EventSeat> eventSeats = new ArrayList<>();
        Random random = new Random();

        for (Seat seat : seats) {
            // Tjek om EventSeat allerede eksisterer for dette Event og Seat
            // Dette forhindrer duplikater, hvis InitData køres flere gange (uden at rydde DB)
            if (!eventSeatRepository.existsByEvent_EventIdAndSeat_SeatId(event.getEventId(), seat.getSeatId())) {
                EventSeat es = new EventSeat();
                es.setEvent(event);
                es.setSeat(seat);
                es.setArea(area);
                if (random.nextDouble() < bookingProbability) {
                    es.setStatus("BOOKED");
                } else {
                    es.setStatus("FREE");
                }
                es.setHeldUntil(null);
                es.setSessionId(null);
                eventSeats.add(es);
            }
        }
        return eventSeats;
    }
}