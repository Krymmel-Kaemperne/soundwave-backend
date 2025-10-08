package dk.hjemmehub.soundwavebackend.DTO;

public class StandingDto {
    private Long areaId;
    private String name;
    private int capacity;
    private int reserved;
    private int available;

    public StandingDto(Long areaId, String name, int capacity, int reserved) {
        this.areaId = areaId;
        this.name = name;
        this.capacity = capacity;
        this.reserved = reserved;
        this.available = capacity - reserved;
    }

    public Long getAreaId() { return areaId; }
    public String getName() { return name; }
    public int getCapacity() { return capacity; }
    public int getReserved() { return reserved; }
    public int getAvailable() { return available; }
}