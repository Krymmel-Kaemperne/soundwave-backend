package dk.hjemmehub.soundwavebackend.Controller;

import dk.hjemmehub.soundwavebackend.DTO.ReservationConfirmationDto;
import dk.hjemmehub.soundwavebackend.DTO.ReservationRequestDto;
import dk.hjemmehub.soundwavebackend.Repository.ReservationRepository;
import dk.hjemmehub.soundwavebackend.Service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkout")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/confirm-booking/test")
    public String test() {
        return "ReservationController virker";
    }

    @PostMapping("/confirm-booking")
    public ResponseEntity<?> confirmBooking(@RequestBody ReservationRequestDto reservationRequest) {
        try {
            ReservationConfirmationDto confirmation = reservationService.createReservation(reservationRequest);
            return ResponseEntity.ok(confirmation);
        } catch (Exception e) {
            e.printStackTrace();
            String message = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
            return ResponseEntity.badRequest().body("Booking failed: " + message);
        }
    }


}
