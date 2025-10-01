package dk.hjemmehub.soundwavebackend.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="event_id")
    private int eventId;

    private String title;
    private String description;
    private String status;
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "hall_id", referencedColumnName = "hall_id")
    private Hall hall;

    public Event() {}

    public Event(int eventId, String title, String description, String status, LocalDateTime eventDate, Hall hall) {
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.eventDate = eventDate;
        this.hall = hall;
    }


    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }




}