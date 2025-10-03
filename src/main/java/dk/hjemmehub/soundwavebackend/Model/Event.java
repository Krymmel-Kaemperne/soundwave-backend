
package dk.hjemmehub.soundwavebackend.Model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="event_id")
    private int eventId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String status;

    @Column(name ="event_date")
    private LocalDateTime eventDate;


    @Column(name="base_price")
    private BigDecimal basePrice;

    private String imageUrl;


    @ManyToOne
    @JoinColumn(name = "hall_id", referencedColumnName = "hall_id")
    private Hall hall;

    public Event() {}



    public Event(String title, String description, BigDecimal basePrice, String status,
                 LocalDateTime eventDate, Hall hall) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.eventDate = eventDate;
        this.hall = hall;
        this.basePrice=basePrice;
    }

    public Event(int eventId, String title, String description, String status, LocalDateTime eventDate, Hall hall, String imageUrl) {
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.imageUrl = imageUrl;
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

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
