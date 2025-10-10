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

    // ✅ FIX HER: Brug LEFT JOIN for at inkludere ståpladser (hvor seat er NULL)
    @Query("SELECT es FROM EventSeat es JOIN FETCH es.event LEFT JOIN FETCH es.seat LEFT JOIN FETCH es.area WHERE es.event.eventId = :eventId")
    List<EventSeat> findByEvent_EventId(Long eventId);

    // Paginated version for large datasets
    // ✅ FIX HER (og muligvis fjern es.seat.area hvis det ikke bruges direkte for ståpladser i paginering)
    @Query(value = "SELECT es FROM EventSeat es LEFT JOIN FETCH es.seat LEFT JOIN FETCH es.area WHERE es.event.eventId = :eventId",
            countQuery = "SELECT COUNT(es) FROM EventSeat es WHERE es.event.eventId = :eventId")
    Page<EventSeat> findByEvent_EventId(Long eventId, Pageable pageable);

    // Optimized query with JOIN FETCH for specific seats - denne er kun for siddepladser, så den kan være en JOIN
    @Query("SELECT es FROM EventSeat es JOIN FETCH es.event JOIN FETCH es.seat JOIN FETCH es.seat.area WHERE es.event.eventId = :eventId AND es.seat.seatId IN :seatIds")
    List<EventSeat> findByEvent_EventIdAndSeat_SeatIdIn(Long eventId, List<Long> seatIds);

    // Også denne bør være LEFT JOIN på seat/area, hvis du vil bruge den til både sidde- og ståpladser
    Optional<EventSeat> findByEvent_EventIdAndSeat_SeatId(Long eventId, Long seatId);

    boolean existsByEvent_EventIdAndSeat_SeatId(Long eventId, Long seatId);

    List<EventSeat> findByEvent_EventIdAndStatusAndSessionId(Long eventId, String status, String sessionId);

    // Denne er med PESSIMISTIC_WRITE, og er sandsynligvis kun for sæder (ikke ståpladser),
    // så en INNER JOIN på seat er nok OK her
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT es FROM EventSeat es JOIN FETCH es.event JOIN FETCH es.seat JOIN FETCH es.seat.area WHERE es.event.eventId = :eventId AND es.seat.seatId IN :seatIds")
    List<EventSeat> findByEvent_EventIdAndSeat_SeatIdInWithLock(Long eventId, List<Long> seatIds);

    List<EventSeat> findByStatusAndHeldUntilBefore(String status, LocalDateTime heldUntil);

    // ✅ VIGTIGT: Denne Native Query er fortsat den bedste måde at tælle/finde konkrete ståpladser
    @Query(value = "SELECT * FROM event_seat es WHERE es.event_id = :eventId AND es.area_id = :areaId AND es.seat_id IS NULL AND es.status = 'FREE' LIMIT :count", nativeQuery = true)
    List<EventSeat> findAvailableStandingSpots(Long eventId, Long areaId, int count);
}