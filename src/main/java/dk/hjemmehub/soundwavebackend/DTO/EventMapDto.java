package dk.hjemmehub.soundwavebackend.DTO;

import java.util.List;

public class EventMapDto {
    private Long eventId;
    private String eventName;
    private String hallName;
    private List<AreaMapDto> areas;
    private Boolean isVisible;

    public EventMapDto(Long eventId, String eventName, String hallName, List<AreaMapDto> areas, Boolean isVisible) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.hallName = hallName;
        this.areas = areas;
        this.isVisible = isVisible;
    }

    public Long getEventId() { return eventId; }
    public String getEventName() { return eventName; }
    public String getHallName() { return hallName; }
    public List<AreaMapDto> getAreas() { return areas; }
    public Boolean getIsVisible() { return isVisible; }
    public void setIsVisible(Boolean isVisible) { this.isVisible = isVisible; }
}


