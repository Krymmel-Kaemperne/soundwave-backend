package dk.hjemmehub.soundwavebackend.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Event_Seat",
       indexes = {
           @Index(name = "idx_event_seat_event_id", columnList = "event_id"),
           @Index(name = "idx_event_seat_seat_id", columnList = "seat_id"),
           @Index(name = "idx_event_seat_status", columnList = "status"),
           @Index(name = "idx_event_seat_event_status", columnList = "event_id, status"),
           @Index(name = "idx_event_seat_session_id", columnList = "session_id")
       })
// Hibernate second-level cache annotation removed when caching disabled
public class EventSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventSeatId;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @Column(name="status")
    private String status;

    @Column(name="held_until")
    private LocalDateTime heldUntil;

    @Column(name="session_id")
    private String sessionId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getHeldUntil() {
        return heldUntil;
    }

    public void setHeldUntil(LocalDateTime heldUntil) {
        this.heldUntil = heldUntil;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    // getters and setters
    public Long getEventSeatId() {
        return eventSeatId;
    }

    public void setEventSeatId(Long eventSeatId) {
        this.eventSeatId = eventSeatId;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public void setReserved(boolean reserved) {
        // Update status based on the boolean value
        if (reserved) {
            this.status = "BOOKED";
        } else {
            this.status = "FREE";
        }
    }

    public boolean isReserved() {
        return "BOOKED".equals(this.status) || "HELD".equals(this.status);
    }
}