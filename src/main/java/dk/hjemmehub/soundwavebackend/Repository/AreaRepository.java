package dk.hjemmehub.soundwavebackend.Repository;

import dk.hjemmehub.soundwavebackend.Model.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AreaRepository extends JpaRepository<Area, Long> {
    
    // Finder alle områder (Area) for en specifik sal (Hall) baseret på salens ID
    // Join fetch sikrer hall hentes i samme databasekald som area-objekter.
    @Query("SELECT a FROM Area a JOIN FETCH a.hall WHERE a.hall.hallId = :hallId")
    List<Area> findByHall_HallId(Long hallId);
}

