package dk.hjemmehub.soundwavebackend.Repository;

import dk.hjemmehub.soundwavebackend.Model.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AreaRepository extends JpaRepository<Area, Long> {
    
    // Optimized query with JOIN FETCH to avoid N+1 problem when loading areas with their hall
    @Query("SELECT a FROM Area a JOIN FETCH a.hall WHERE a.hall.hallId = :hallId")
    List<Area> findByHall_HallId(Long hallId);
}

