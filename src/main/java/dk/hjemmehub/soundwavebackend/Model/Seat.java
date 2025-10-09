package dk.hjemmehub.soundwavebackend.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "Seat",
       indexes = {
           @Index(name = "idx_seat_area_id", columnList = "area_id"),
           @Index(name = "idx_seat_row_seat", columnList = "row_no, seat_no"),
           @Index(name = "idx_seat_area_row", columnList = "area_id, row_no")
       })
// Hibernate second-level cache annotation removed when caching disabled
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;

    @Column(name = "row_no")
    private int rowNumber;

    @Column(name = "seat_no")
    private int seatNumber;

    @ManyToOne
    @JoinColumn(name = "area_id")  // kobler til area_id i databasen
    private Area area;

    // getters and setters
    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }
}