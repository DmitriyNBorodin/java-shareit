package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.util.BookingTime;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private Long bookingId;
    @BookingTime(message = "Время начала бронирования уже прошло")
    @NotNull(message = "Время начала бронирования должно быть указано")
    private LocalDateTime start;
    @BookingTime(message = "Время бронирования уже прошло")
    @NotNull(message = "Время окончания бронирования должно быть указано")
    private LocalDateTime end;
    private Long itemId;
    private Long bookerId;
    private BookingStatus status;
}
