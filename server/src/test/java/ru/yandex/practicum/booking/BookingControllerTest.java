package ru.yandex.practicum.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.booking.dto.BookingDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private BookingServiceImpl bookingService;

    @Test
    void addNewBookingTest() throws Exception {
        BookingDto bookingDto = BookingDto.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(1))
                .itemId(1L)
                .bookerId(1L)
                .build();
        Booking booking = Booking.builder()
                .id(1L)
                .status(BookingStatus.WAITING)
                .build();
        when(bookingService.addNewBooking(Mockito.anyLong(), Mockito.any(BookingDto.class))).thenReturn(booking);

        mvc.perform(post("/bookings").content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.status", is("WAITING"), BookingStatus.class));
    }

    @Test
    void approveBookingTest() throws Exception {
        Booking booking = Booking.builder()
                .id(1L)
                .status(BookingStatus.APPROVED)
                .build();
        when(bookingService.approveBooking(1L, 1L, "true"))
                .thenReturn(booking);

        mvc.perform(patch("/bookings/{bookingId}", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("APPROVED"), BookingStatus.class));
    }

    @Test
    void getBookingTest() throws Exception {
        Booking booking = Booking.builder()
                .id(2L)
                .status(BookingStatus.WAITING)
                .build();
        when(bookingService.getBookingById(1L, 2L)).thenReturn(booking);

        mvc.perform(get("/bookings/{bookingId}", 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2L), Long.class));
    }

    @Test
    void getBookingsByUserIdTest() throws Exception {
        Booking booking = Booking.builder()
                .id(2L)
                .status(BookingStatus.WAITING)
                .build();
        when(bookingService.getBookingsByUserId(1L, "WAITING")).thenReturn(List.of(booking));

        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .param("status", "WAITING")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsByOwnerIdTest() throws Exception {
        Booking booking = Booking.builder()
                .id(2L)
                .status(BookingStatus.APPROVED)
                .build();
        when(bookingService.getBookingsByOwnerId(1L, "APPROVED")).thenReturn(List.of(booking));

        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}