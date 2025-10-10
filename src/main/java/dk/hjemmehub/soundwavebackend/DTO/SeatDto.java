package dk.hjemmehub.soundwavebackend.DTO;

public class SeatDto {
    private Long seatId;
    private int rowNumber;
    private int seatNumber;
    private String status;
    private String label;

    public SeatDto(Long seatId, int rowNumber, int seatNumber, String status, String label) {
        this.seatId = seatId;
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.status = status;
        this.label = label;
    }

    public Long getSeatId() {
        return seatId;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public String getStatus() {
        return status;
    }

    public String getLabel() {
        return label;
    }
}
