package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

@Component
@RequiredArgsConstructor
public class BookingDtoMapper {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .bookingId(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .itemId(booking.getItem().getId())
                .bookerId(booking.getBooker().getId())
                .status(booking.getStatus())
                .build();
    }

    public Booking toBooking(BookingDto bookingDto) {
        return Booking.builder()
                .id(bookingDto.getBookingId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(itemRepository.getReferenceById(bookingDto.getItemId()))
                .booker(userRepository.getReferenceById(bookingDto.getBookerId()))
                .status(bookingDto.getStatus())
                .build();
    }
}
