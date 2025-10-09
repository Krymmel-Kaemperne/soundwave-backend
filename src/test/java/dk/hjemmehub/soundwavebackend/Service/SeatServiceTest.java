package dk.hjemmehub.soundwavebackend.Service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class SeatServiceTest {

    private SeatService seatService;

    @BeforeEach
    void setupMock() {
        seatService = new SeatService(null, null, null, null, null);
    }

    @Test
    void isBalcony_shouldReturnTrueForNamesContainingBalcony() throws Exception {
        var method = SeatService.class.getDeclaredMethod("isBalcony", String.class);
        method.setAccessible(true);

        boolean result1 = (boolean) method.invoke(seatService, "Main Balcony Area");
        boolean result2 = (boolean) method.invoke(seatService, "Øvre Balkong Sektion");
        boolean result3 = (boolean) method.invoke(seatService, "Floor level seating");

        assertTrue(result1);
        assertTrue(result2);
        assertFalse(result3);
    }

    @Test
    void computeAreaPrice_shouldAdd200ForBalconyAreas() throws Exception {
        var method = SeatService.class.getDeclaredMethod("computeAreaPrice", BigDecimal.class, String.class);
        method.setAccessible(true);

        BigDecimal basePrice = BigDecimal.valueOf(800);
        BigDecimal priceForBalcony = (BigDecimal) method.invoke(seatService, basePrice, "Main Balcony");
        BigDecimal priceForNormal = (BigDecimal) method.invoke(seatService, basePrice, "Main Floor");

        assertEquals(BigDecimal.valueOf(1000), priceForBalcony); // 800 + 200
        assertEquals(BigDecimal.valueOf(800), priceForNormal);
    }

    @Test
    void computeAreaPrice_shouldHandleNullBasePrice() throws Exception {
        var method = SeatService.class.getDeclaredMethod("computeAreaPrice", BigDecimal.class, String.class );
        method.setAccessible(true);

        BigDecimal result = (BigDecimal) method.invoke(seatService, null, "Balcony A");

        assertEquals(BigDecimal.valueOf(200), result);
    }
    
}
