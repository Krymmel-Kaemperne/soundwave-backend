package dk.hjemmehub.soundwavebackend.DTO;

public class AreaDto {
    private Long areaId;
    private String name;
    private String type;
    private Integer capacity;

    public AreaDto(Long areaId, String name, String type, Integer capacity) {
        this.areaId = areaId;
        this.name = name;
        this.type = type;
        this.capacity = capacity;
    }

    public Long getAreaId() {
        return areaId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Integer getCapacity() {
        return capacity;
    }
}


