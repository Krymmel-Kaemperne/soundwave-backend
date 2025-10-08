package dk.hjemmehub.soundwavebackend.Repository;

import dk.hjemmehub.soundwavebackend.Model.EventSeat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import jakarta.persistence.LockModeType;
import java.util.List;

public interface EventSeatRepository extends JpaRepository<EventSeat, Long> {
    
    // Optimized query with JOIN FETCH to avoid N+1 problem
    @Query("SELECT es FROM EventSeat es JOIN FETCH es.event JOIN FETCH es.seat JOIN FETCH es.seat.area WHERE es.event.eventId = :eventId")
    List<EventSeat> findByEvent_EventId(Long eventId);
    
    // Paginated version for large datasets
    @Query(value = "SELECT es FROM EventSeat es WHERE es.event.eventId = :eventId",
           countQuery = "SELECT COUNT(es) FROM EventSeat es WHERE es.event.eventId = :eventId")
    Page<EventSeat> findByEvent_EventId(Long eventId, Pageable pageable);
    
    // Optimized query with JOIN FETCH for specific seats
    @Query("SELECT es FROM EventSeat es JOIN FETCH es.event JOIN FETCH es.seat JOIN FETCH es.seat.area WHERE es.event.eventId = :eventId AND es.seat.seatId IN :seatIds")
    List<EventSeat> findByEvent_EventIdAndSeat_SeatIdIn(Long eventId, List<Long> seatIds);

    boolean existsByEvent_EventIdAndSeat_SeatId(Long eventId, Long seatId);

    // find sæder holdt af en specifik session, så vi kan frigøre dem.
    List<EventSeat> findByEvent_EventIdAndStatusAndSessionId(Long eventId, String status, String sessionId);
    
    // Find seats with pessimistic write lock to prevent race conditions during seat holding
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT es FROM EventSeat es JOIN FETCH es.event JOIN FETCH es.seat JOIN FETCH es.seat.area WHERE es.event.eventId = :eventId AND es.seat.seatId IN :seatIds")
    List<EventSeat> findByEvent_EventIdAndSeat_SeatIdInWithLock(Long eventId, List<Long> seatIds);
}
