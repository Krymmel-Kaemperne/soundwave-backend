package dk.hjemmehub.soundwavebackend.Service;


import dk.hjemmehub.soundwavebackend.DTO.ReservationRequestDto;
import dk.hjemmehub.soundwavebackend.Model.EventSeat;
import dk.hjemmehub.soundwavebackend.Model.Seat;
import dk.hjemmehub.soundwavebackend.Repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventSeatRepository eventSeatRepository;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private ReservationSeatRepository reservationSeatRepository;

    @Mock
    private ReservationAreaRepository reservationAreaRepositry;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setupMocks() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void createReservation_booksSeatSuccusfully(){
        // 1. Forbereder TestData
        //Lav en Fake request
        var request = new ReservationRequestDto();
        request.setEventId(1L);
        request.setCustomerName("Peyton Hunter");
        request.setCustomerEmail("peyton@example.com");
        request.setTotalPrice(1234.0);
        request.setSeatIds(java.util.List.of(10L));

        //Lav et sæde og et eventSeat objekt
        var seat = new Seat();
        seat.setSeatId(10L);

        var eventSeat = new EventSeat();
        eventSeat.setSeat(seat);
        eventSeat.setStatus("FREE");

        //Fortæl hvad Repo skal gøre
        when(seatRepository.findById(10L)).thenReturn(Optional.of(seat));
        when(eventSeatRepository.findByEvent_EventIdAndSeat_SeatId(1L, 10L))
                .thenReturn(Optional.of(eventSeat));
        when(reservationRepository.save(any())).thenAnswer(i -> i.getArgument(0));


        var result = reservationService.createReservation(request);

        assertNotNull(result);
        assertEquals("Peyton Hunter", result.getCustomerName());
        assertEquals("peyton@example.com", result.getCustomerEmail());
        assertEquals("confirmed", result.getStatus());
        assertEquals("BOOKED", eventSeat.getStatus());

        verify(reservationRepository).save(any());
        verify(eventSeatRepository).save(any());
    }

    @Test
    void createReservation_ShouldThrowException_whenEventIdIsNull()
    {
        var request = new ReservationRequestDto();
        request.setEventId(null);
        request.setCustomerName("Sebastian Boel");
        request.setCustomerEmail("Sebastian@gmail.com");

        var exception = assertThrows(RuntimeException.class,
                () -> reservationService.createReservation(request));
        assertEquals("EventId cannot be null!", exception.getMessage());
    }

}
