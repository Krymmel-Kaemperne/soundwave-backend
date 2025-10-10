package dk.hjemmehub.soundwavebackend.Service;

import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class SessionService {

    public String generateUniqueSessionId() {
        // Genererer en universelt unik ID for brugere
        return UUID.randomUUID().toString();
    }
}