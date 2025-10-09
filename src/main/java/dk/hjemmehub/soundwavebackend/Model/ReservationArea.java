package dk.hjemmehub.soundwavebackend.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "reservation_area")
public class ReservationArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reservation_id")
    private Long reservationId;

    @Column(name = "area_id")
    private Long areaId;

    @Column(name = "standing_count")
    private Integer standingCount;

    // --- Constructors ---
    public ReservationArea() {}

    // --- Getters & Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Integer getStandingCount() {
        return standingCount;
    }

    public void setStandingCount(Integer standingCount) {
        this.standingCount = standingCount;
    }
}