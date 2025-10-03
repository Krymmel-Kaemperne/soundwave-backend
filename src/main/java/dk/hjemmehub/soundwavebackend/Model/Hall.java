package dk.hjemmehub.soundwavebackend.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class Hall {

    @Id
    @Column(name = "hall_id")
    private int hallId;

    private String name;
    private int capacity;
    private String location;

    public Hall() {}

    public Hall(int hallId, String name, int capacity, String location) {
        this.hallId = hallId;
        this.name = name;
        this.capacity = capacity;
        this.location = location;
    }

    public int getHallId() {
        return hallId;
    }

    public void setHallId(int hallId) {
        this.hallId = hallId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
public class Hall {
}
