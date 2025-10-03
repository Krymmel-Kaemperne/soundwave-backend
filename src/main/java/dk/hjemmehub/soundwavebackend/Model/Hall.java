package dk.hjemmehub.soundwavebackend.Model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Hall")
public class Hall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hallId;

    private String name;
    private String description;

    @OneToMany(mappedBy = "hall")
    private List<Area> areas;

    // getters/setters
    public Long getId() {
        return hallId;
    }

    public void setId(Long hallId) {
        this.hallId = hallId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Area> getArea() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }
}
