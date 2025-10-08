package dk.hjemmehub.soundwavebackend.DTO;

import java.util.List;

public class SeatReservationRequest {
    private List<Long> seatIds;

    public SeatReservationRequest() {}

    public SeatReservationRequest(List<Long> seatIds) {
        this.seatIds = seatIds;
    }

    public List<Long> getSeatIds() {
        return seatIds;
    }

    public void setSeatIds(List<Long> seatIds) {
        this.seatIds = seatIds;
    }
}


