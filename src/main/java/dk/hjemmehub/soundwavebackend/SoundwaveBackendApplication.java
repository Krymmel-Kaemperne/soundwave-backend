package dk.hjemmehub.soundwavebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SoundwaveBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoundwaveBackendApplication.class, args);
    }

}
