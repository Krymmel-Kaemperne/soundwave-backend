package dk.hjemmehub.soundwavebackend.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "Event_Seat")
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

    private boolean isReserved;

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

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }
}