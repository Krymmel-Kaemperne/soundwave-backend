package dk.hjemmehub.soundwavebackend.Service;

import dk.hjemmehub.soundwavebackend.Model.EventSeat;
import dk.hjemmehub.soundwavebackend.Repository.EventSeatRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CleanupService {
    private final EventSeatRepository eventSeatRepository;

    @Autowired
    public CleanupService(EventSeatRepository eventSeatRepository) {
        this.eventSeatRepository = eventSeatRepository;
    }

    @Scheduled(fixedRate = 60000) // Kører hvert 60. sekund (60000 ms)
    @Transactional // Sikrer at alle databaseoperationer er atomare
    public void releaseExpiredSeatHolds() {
        LocalDateTime now = LocalDateTime.now();

        List<EventSeat> expiredHolds = eventSeatRepository.findByStatusAndHeldUntilBefore("HELD", now);

        if (!expiredHolds.isEmpty()) {
            System.out.println("Frigiver " + expiredHolds.size() + " udløbne sæde-holds.");
            for (EventSeat es : expiredHolds) {
                es.setStatus("FREE");
                es.setHeldUntil(null);
                es.setSessionId(null);
            }
            eventSeatRepository.saveAll(expiredHolds);
        }
    }
}
