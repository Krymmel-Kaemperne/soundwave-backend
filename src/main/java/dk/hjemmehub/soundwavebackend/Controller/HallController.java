package dk.hjemmehub.soundwavebackend.Controller;

import dk.hjemmehub.soundwavebackend.Model.Hall;
import dk.hjemmehub.soundwavebackend.Repository.HallRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/halls")
public class HallController {
   private final HallRepository hallRepository;

   public HallController(HallRepository hallRepository) {
       this.hallRepository = hallRepository;
   }

   @GetMapping
    public List<Hall>getAllHalls() {
       return hallRepository.findAll();
   }

   @GetMapping ("/{hallId}")
    public Hall getHallById(@PathVariable Long hallId) {
       return hallRepository.findById(hallId)
               .orElseThrow(() -> new RuntimeException("Hall not found"));
   }

}
