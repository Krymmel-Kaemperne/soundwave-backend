package dk.hjemmehub.soundwavebackend.DTO;

import java.util.List;

public class SeatHoldRequestDTO {
    private List<Long> seatIds;
    private String sessionId; // Den session ID kunden allerede har (fra frontend localStorage f.eks.)

    public SeatHoldRequestDTO() {}

    public SeatHoldRequestDTO(List<Long> seatIds, String sessionId) {
        this.seatIds = seatIds;
        this.sessionId = sessionId;
    }

    // --- Getters ---
    public List<Long> getSeatIds() {
        return seatIds;
    }

    public String getSessionId() {
        return sessionId;
    }

    // --- Setters ---
    public void setSeatIds(List<Long> seatIds) {
        this.seatIds = seatIds;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}