package dk.hjemmehub.soundwavebackend.Repository;

import dk.hjemmehub.soundwavebackend.Model.EventSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventSeatRepository extends JpaRepository<EventSeat, Long> {
    List<EventSeat> findByEvent_EventId(Long eventId);
    List<EventSeat> findByEvent_EventIdAndSeat_SeatIdIn(Long eventId, List<Long> seatIds);

    boolean existsByEvent_EventIdAndSeat_SeatId(Long eventId, Long seatId);

    // find sæder holdt af en specifik session, så vi kan frigøre dem.
    List<EventSeat> findByEvent_EventIdAndStatusAndSessionId(Long eventId, String status, String sessionId);
}
