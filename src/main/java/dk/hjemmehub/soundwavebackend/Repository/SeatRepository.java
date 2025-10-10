package dk.hjemmehub.soundwavebackend.Repository;

import dk.hjemmehub.soundwavebackend.Model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    // Tjekker om der eksisterer sæder tilhørende et specifikt område.
    boolean existsByArea_AreaId(Long areaId);

    // Finder alle sæder for et specifikt område og inkluderer det tilhørende område (Area) i samme kald for optimering.
    @Query("SELECT s FROM Seat s JOIN FETCH s.area WHERE s.area.areaId = :areaId")
    List<Seat> findByArea_AreaId(Long areaId);
}


