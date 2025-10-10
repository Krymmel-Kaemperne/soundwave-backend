package dk.hjemmehub.soundwavebackend.DTO;

import java.util.List;

public class ReservationRequestDto {

    private List<Long> seatIds;
    private List<StandingDto> standingAreas;
    private String customerName;
    private String customerEmail;
    private Long eventId;
    private Double totalPrice;

    public ReservationRequestDto() {}

    public ReservationRequestDto(List<Long> seatIds, List<StandingDto> standingAreas,
                                 String customerName, String customerEmail,
                                 Long eventId, Double totalPrice) {
        this.seatIds = seatIds;
        this.standingAreas = standingAreas;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.eventId = eventId;
        this.totalPrice = totalPrice;
    }

    public List<Long> getSeatIds() {
        return seatIds;
    }

    public void setSeatIds(List<Long> seatIds) {
        this.seatIds = seatIds;
    }

    public List<StandingDto> getStandingAreas() {
        return standingAreas;
    }

    public void setStandingAreas(List<StandingDto> standingAreas) {
        this.standingAreas = standingAreas;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}