package dk.hjemmehub.soundwavebackend.Repository;

import dk.hjemmehub.soundwavebackend.Model.EventSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventSeatRepository extends JpaRepository<EventSeat, Long> {
    List<EventSeat> findByEvent_EventId(Long eventId);

}
