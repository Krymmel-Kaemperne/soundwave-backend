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

    //DEN RIGTIGE METODE TIL DATABASEN
//    public List<AreaDto> getAreasForHall(Long hallId) {
//        var areas = areaRepository.findByHall_Id(hallId);
//
//        return areas.stream()
//                .map(a -> new AreaDto(
//                        a.getAreaId(),
//                        a.getName(),
//                        a.getType(),
//                        a.getCapacity()
//                ))
//                .toList();
//    }

    public List<AreaDto> getAreasForHall(Long hallId) {
        return List.of(
                new AreaDto(1L, "Standing Floor", "standing", 1000),
                new AreaDto(2L, "VIP Balcony", "seating", null)
        );
    }

    //FAKE
    public StandingDto getStandingAreaFake(Long areaId) {
        // bare fake data – forestil dig det er dit "Standing Floor"
        return new StandingDto(areaId, "Standing Area", 1000, 150);
    }



}
