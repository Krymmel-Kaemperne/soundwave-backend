package dk.hjemmehub.soundwavebackend.Controller;

import dk.hjemmehub.soundwavebackend.DTO.AreaDto;
import dk.hjemmehub.soundwavebackend.DTO.StandingDto;
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
    private final SeatService seatService;


    public AreaController(AreaService areaService, SeatService seatService) {
        this.areaService = areaService;
        this.seatService = seatService;
    }

    @GetMapping("/{hallId}/areas")
    public List<AreaDto> getAreasForHall(@PathVariable Long hallId) {
        return areaService.getAreasForHall(hallId);
    }

    // POST /halls/{hallId}/areas/{areaId}/setup?rows=10&cols=20
    @GetMapping("/{hallId}/areas/{areaId}/setup/{rows}/{cols}")
    public String setupSeatsForArea(@PathVariable Long hallId, @PathVariable Long areaId, @PathVariable int rows, @PathVariable int cols) {
        // hallId currently unused; areaId is authoritative
        seatService.setupSeats(areaId, rows, cols);
        return "setup-complete";
    }

    // GET /halls/1/standing
    @GetMapping("/{hallId}/standing")
    public StandingDto getStanding(@PathVariable Long hallId) {
        // bruger hallId som “areaId" her i fake-test
        return areaService.getStandingAreaFake(hallId);
    }


}
