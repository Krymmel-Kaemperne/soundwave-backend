package dk.hjemmehub.soundwavebackend.Model;

import dk.hjemmehub.soundwavebackend.Model.Hall;
import dk.hjemmehub.soundwavebackend.Model.Seat;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Area")
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long areaId;

    private String name;        // fx "VIP Balcony", "Standing Area"
    private String type;        // "seating" eller "standing"
    private Integer capacity;   // bruges kun hvis type = standing

    // relation til Hall
    @ManyToOne
    @JoinColumn(name = "hall_id")
    private Hall hall;

    // hvis area er seating → seats
    @OneToMany(mappedBy = "area", fetch = FetchType.EAGER)
    private List<Seat> seats;

    // getters/setters
    public Long getAreaId() {
        return areaId;
    }

    public void setId(Long areaId) {
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