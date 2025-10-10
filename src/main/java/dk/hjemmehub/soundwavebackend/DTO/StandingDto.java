package dk.hjemmehub.soundwavebackend.DTO;

public class StandingDto {

    private Long areaId;
    private String name;
    private Integer capacity;
    private Integer reserved;
    private Integer available;
    private Integer count;

    public StandingDto() {}

    public StandingDto(Long areaId, String name, Integer capacity, Integer reserved) {
        this.areaId = areaId;
        this.name = name;
        this.capacity = capacity;
        this.reserved = reserved;
        this.available = (capacity != null && reserved != null)
                ? capacity - reserved
                : null;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getReserved() {
        return reserved;
    }

    public void setReserved(Integer reserved) {
        this.reserved = reserved;
    }

    public Integer getAvailable() {
        if (capacity != null && reserved != null) {
            available = capacity - reserved;
        }
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "StandingDto{" +
                "areaId=" + areaId +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                ", reserved=" + reserved +
                ", available=" + available +
                ", count=" + count +
                '}';
    }
}