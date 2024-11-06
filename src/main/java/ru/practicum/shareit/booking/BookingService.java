package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    Booking addNewBooking(Long userId, BookingDto newBookingDto);

    Booking approveBooking(Long userId, Long bookingId, String approved);

    Booking getBookingById(Long userId, Long bookingId);

    List<Booking> getBookingsByUserId(Long userId, String state);

    List<Booking> getBookingsByOwnerId(Long userId, String state);
}
