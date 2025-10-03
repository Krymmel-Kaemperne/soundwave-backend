package dk.hjemmehub.soundwavebackend.Controller;

import dk.hjemmehub.soundwavebackend.DTO.AreaDto;
import dk.hjemmehub.soundwavebackend.DTO.SeatDto;
import dk.hjemmehub.soundwavebackend.DTO.StandingDto;
import dk.hjemmehub.soundwavebackend.Model.Area;
import dk.hjemmehub.soundwavebackend.Service.AreaService;
import dk.hjemmehub.soundwavebackend.Service.SeatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/halls")
public class AreaController {

    private final AreaService areaService;


    public AreaController(AreaService areaService) {
        this.areaService = areaService;
    }

    @GetMapping("/{hallId}/areas")
    public List<AreaDto> getAreasForHall(@PathVariable Long hallId) {
        return areaService.getAreasForHall(hallId);
    }

    // GET /halls/1/standing
    @GetMapping("/{hallId}/standing")
    public StandingDto getStanding(@PathVariable Long hallId) {
        // bruger hallId som “areaId" her i fake-test
        return areaService.getStandingAreaFake(hallId);
    }


}
