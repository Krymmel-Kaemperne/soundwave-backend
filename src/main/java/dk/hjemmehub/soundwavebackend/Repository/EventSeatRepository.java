package dk.hjemmehub.soundwavebackend.Repository;

import dk.hjemmehub.soundwavebackend.Model.EventSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventSeatRepository extends JpaRepository<EventSeat, Long> {
    List<EventSeat> findByEvent_EventId(Long eventId);
    List<EventSeat> findByEvent_EventIdAndSeat_SeatIdIn(Long eventId, List<Long> seatIds);
    Optional<EventSeat> findByEvent_EventIdAndSeat_SeatId(Long eventId, Long seatId);
    boolean existsByEvent_EventIdAndSeat_SeatId(Long eventId, Long seatId);
}