package dk.hjemmehub.soundwavebackend.Model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Area",
       indexes = {
           @Index(name = "idx_area_hall_id", columnList = "hall_id"),
           @Index(name = "idx_area_type", columnList = "type"),
           @Index(name = "idx_area_hall_type", columnList = "hall_id, type")
       })

public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long areaId;

    private String name;
    private String type;
    private Integer capacity;

    @ManyToOne
    @JoinColumn(name = "hall_id")
    private Hall hall;

    @OneToMany(mappedBy = "area", fetch = FetchType.EAGER)
    private List<Seat> seats;

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
}