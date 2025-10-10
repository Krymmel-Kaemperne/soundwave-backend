package dk.hjemmehub.soundwavebackend.Service;

import dk.hjemmehub.soundwavebackend.DTO.AreaDto;
import dk.hjemmehub.soundwavebackend.DTO.StandingDto;
import dk.hjemmehub.soundwavebackend.Repository.AreaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaService {
    private final AreaRepository areaRepository;

    public AreaService(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    public List<AreaDto> getAreasForHall(Long hallId) {
        var areas = areaRepository.findByHall_HallId(hallId);

        return areas.stream()
                .map(a -> new AreaDto(
                        a.getAreaId(),
                        a.getName(),
                        a.getType(),
                        a.getCapacity()
                ))
                .toList();
    }
}
