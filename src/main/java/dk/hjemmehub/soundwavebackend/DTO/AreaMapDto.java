package dk.hjemmehub.soundwavebackend.DTO;

import java.util.List;

public class AreaMapDto {
    private Long areaId;
    private String areaName;
    private String type;
    private Integer capacity;
    private int bookedCount;
    private Double price;
    private List<SeatMapDto> seats;

    public AreaMapDto(Long areaId, String areaName, String type, Integer capacity, int bookedCount, Double price, List<SeatMapDto> seats) {
        this.areaId = areaId;
        this.areaName = areaName;
        this.type = type;
        this.capacity = capacity;
        this.bookedCount = bookedCount;
        this.price = price;
        this.seats = seats;
    }

    public Long getAreaId() { return areaId; }
    public String getAreaName() { return areaName; }
    public String getType() { return type; }
    public Integer getCapacity() { return capacity; }
    public int getBookedCount() { return bookedCount; }
    public Double getPrice() { return price; }
    public List<SeatMapDto> getSeats() { return seats; }
}


