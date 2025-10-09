package dk.hjemmehub.soundwavebackend.Repository;

import dk.hjemmehub.soundwavebackend.Model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    boolean existsByArea_AreaId(Long areaId);

    // Optimized query with JOIN FETCH to avoid N+1 problem when loading seats with their area
    @Query("SELECT s FROM Seat s JOIN FETCH s.area WHERE s.area.areaId = :areaId")
    List<Seat> findByArea_AreaId(Long areaId);
}


