package dk.hjemmehub.soundwavebackend.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Event",
       indexes = {
           @Index(name = "idx_event_hall_id", columnList = "hall_id"),
           @Index(name = "idx_event_date", columnList = "event_date"),
           @Index(name = "idx_event_status", columnList = "status"),
           @Index(name = "idx_event_visible", columnList = "is_visible")
       })

public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long eventId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String status;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Column(name = "base_price")
    private BigDecimal basePrice;

    private String imageUrl;

    @Column(name = "is_visible", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isVisible = true;

    @ManyToOne
    @JoinColumn(name = "hall_id", referencedColumnName = "hall_id")
    private Hall hall;

    public Event() {}

    public Event(String title, String description, BigDecimal basePrice, String status, LocalDateTime eventDate, Hall hall) {
        this.title = title;
        this.description = description;
        this.basePrice = basePrice;
        this.status = status;
        this.eventDate = eventDate;
        this.hall = hall;
        this.isVisible = true;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
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

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
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

    public Boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }

}