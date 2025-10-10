package dk.hjemmehub.soundwavebackend.DTO;

import java.util.List;

public class SeatHoldRequestDTO {
    private List<Long> seatIds;
    private String sessionId;

    public SeatHoldRequestDTO() {}

    public SeatHoldRequestDTO(List<Long> seatIds, String sessionId) {
        this.seatIds = seatIds;
        this.sessionId = sessionId;
    }

    public List<Long> getSeatIds() {
        return seatIds;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSeatIds(List<Long> seatIds) {
        this.seatIds = seatIds;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}