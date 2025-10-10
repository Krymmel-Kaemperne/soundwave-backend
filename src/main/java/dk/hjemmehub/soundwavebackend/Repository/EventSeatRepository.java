package dk.hjemmehub.soundwavebackend.Repository;

import dk.hjemmehub.soundwavebackend.Model.EventSeat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import jakarta.persistence.LockModeType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventSeatRepository extends JpaRepository<EventSeat, Long> {

    // Finder alle EventSeats for et event. Inkl. ståpladser hvor sædet er (NULL).
    @Query("SELECT es FROM EventSeat es JOIN FETCH es.event LEFT JOIN FETCH es.seat LEFT JOIN FETCH es.area WHERE es.event.eventId = :eventId")
    List<EventSeat> findByEvent_EventId(Long eventId);

    // Paginated version.
    @Query(value = "SELECT es FROM EventSeat es LEFT JOIN FETCH es.seat LEFT JOIN FETCH es.area WHERE es.event.eventId = :eventId",
            countQuery = "SELECT COUNT(es) FROM EventSeat es WHERE es.event.eventId = :eventId")
    Page<EventSeat> findByEvent_EventId(Long eventId, Pageable pageable);

    // Finder EventSeats for specifikke siddepladser inden for et event, inkl. tilhørende Seat/Area/Event.
    @Query("SELECT es FROM EventSeat es JOIN FETCH es.event JOIN FETCH es.seat JOIN FETCH es.seat.area WHERE es.event.eventId = :eventId AND es.seat.seatId IN :seatIds")
    List<EventSeat> findByEvent_EventIdAndSeat_SeatIdIn(Long eventId, List<Long> seatIds);

    // Finder en enkelt EventSeat for et specifikt event og sæde ID.
    Optional<EventSeat> findByEvent_EventIdAndSeat_SeatId(Long eventId, Long seatId);

    // Tjekker om en EventSeat eksisterer for et givet event og sæde ID.
    boolean existsByEvent_EventIdAndSeat_SeatId(Long eventId, Long seatId);

    // Finder EventSeats holdt af en specifik session for et event.
    List<EventSeat> findByEvent_EventIdAndStatusAndSessionId(Long eventId, String status, String sessionId);

    // Finder EventSeats for specifikke siddepladser med pessimistisk lås for at forhindre race conditions.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT es FROM EventSeat es JOIN FETCH es.event JOIN FETCH es.seat JOIN FETCH es.seat.area WHERE es.event.eventId = :eventId AND es.seat.seatId IN :seatIds")
    List<EventSeat> findByEvent_EventIdAndSeat_SeatIdInWithLock(Long eventId, List<Long> seatIds);

    // Finder EventSeats med status "HELD", hvis hold-tiden er udløbet.
    List<EventSeat> findByStatusAndHeldUntilBefore(String status, LocalDateTime heldUntil);

    // Native query til at finde et specifikt antal ledige ståpladser for et event og område.
    @Query(value = "SELECT * FROM event_seat es WHERE es.event_id = :eventId AND es.area_id = :areaId AND es.seat_id IS NULL AND es.status = 'FREE' LIMIT :count", nativeQuery = true)
    List<EventSeat> findAvailableStandingSpots(Long eventId, Long areaId, int count);
}