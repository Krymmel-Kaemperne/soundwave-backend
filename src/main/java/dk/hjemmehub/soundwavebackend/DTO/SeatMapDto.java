package dk.hjemmehub.soundwavebackend.DTO;

public class SeatMapDto {
    private Long seatId;
    private int rowNumber;
    private int seatNumber;
    private String status;
    private String label;
    private Long areaId;
    private Double price;

    public SeatMapDto(Long seatId, int rowNumber, int seatNumber, String status, String label, Long areaId, Double price) {
        this.seatId = seatId;
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.status = status;
        this.label = label;
        this.areaId = areaId;
        this.price = price;
    }

    public Long getSeatId() { return seatId; }
    public int getRowNumber() { return rowNumber; }
    public int getSeatNumber() { return seatNumber; }
    public String getStatus() { return status; }
    public String getLabel() { return label; }
    public Long getAreaId() { return areaId; }
    public Double getPrice() { return price; }
}


