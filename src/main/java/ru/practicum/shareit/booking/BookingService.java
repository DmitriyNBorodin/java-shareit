package ru.practicum.shareit.booking;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookingDtoMapper bookingDtoMapper;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public Booking addNewBooking(Long userId, BookingDto newBookingDto) {
        log.info("Новый запрос на бронирование {}", newBookingDto);
        if (newBookingDto.getStart().equals(newBookingDto.getEnd())) {
            throw new ValidationException("Время начала и окончания бронирования должно различаться");
        }
        Optional<Item> itemForBooking = itemRepository.getItemById(newBookingDto.getItemId());
        if (userRepository.findUserById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        if (itemForBooking.isEmpty()) {
            throw new ItemNotFoundException("Предмет с запрашиваемым ID отсутствует");
        }
        if (!itemForBooking.get().getAvailable()) {
            throw new ValidationException("Запрашиваемый предмет недоступен");
        }
        newBookingDto.setStatus(BookingStatus.WAITING);
        newBookingDto.setBookerId(userId);
        Booking newBooking = bookingDtoMapper.toBooking(newBookingDto);
        return bookingRepository.save(newBooking);
    }

    public Booking approveBooking(Long userId, Long bookingId, String approved) {
        Item itemToBook = bookingRepository.getItemById(bookingId);
        if (!Objects.equals(itemToBook.getOwnerId(), userId)) {
            throw new ValidationException("Подтвердить статус бронирования может только владелец");
        }
        Booking bookingToApprove = bookingRepository.getReferenceById(bookingId);
        boolean approveBooking = Boolean.parseBoolean(approved);
        if (approveBooking) {
            bookingToApprove.setStatus(BookingStatus.APPROVED);
        } else {
            bookingToApprove.setStatus(BookingStatus.REJECTED);
        }
        return bookingRepository.save(bookingToApprove);
    }

    public Booking getBookingById(Long userId, Long bookingId) {
        Item bookedItem = bookingRepository.getItemById(bookingId);
        Booking requiredBooking = bookingRepository.getReferenceById(bookingId);
        if (bookedItem.getOwnerId() != userId && requiredBooking.getBooker().getId() != userId) {
            throw new ValidationException("Просматривать состояние бронирования может только его создатель " +
                                          "или владалец предмета");
        }
        return requiredBooking;
    }

    public List<Booking> getBookingsByUserId(Long userId, String state) {
        List<Booking> allUsersBookings = bookingRepository.findBookingByBookerId(userId);
        BookingState bookingState = BookingState.valueOf(state);
        return sortBookingsByState(allUsersBookings, bookingState);
    }

    public List<Booking> getBookingsByOwnerId(Long userId, String state) {
        if (userRepository.findUserById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        List<Booking> allOwnersBookings = bookingRepository.findBookingByOwnerId(userId);
        BookingState bookingState = BookingState.valueOf(state);
        return sortBookingsByState(allOwnersBookings, bookingState);
    }

    private List<Booking> sortBookingsByState(List<Booking> allBookings, BookingState bookingState) {
        return switch (bookingState) {
            case BookingState.CURRENT ->
                    allBookings.stream().filter(booking -> booking.getStart().isBefore(LocalDateTime.now())
                                                           && booking.getEnd().isAfter(LocalDateTime.now()))
                            .sorted(Comparator.comparing(Booking::getEnd)).toList();
            case BookingState.PAST ->
                    allBookings.stream().filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                            .sorted(Comparator.comparing(Booking::getEnd)).toList();
            case BookingState.FUTURE ->
                    allBookings.stream().filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                            .sorted(Comparator.comparing(Booking::getEnd)).toList();
            case BookingState.WAITING ->
                    allBookings.stream().filter(booking -> booking.getStatus().equals(BookingStatus.WAITING))
                            .sorted(Comparator.comparing(Booking::getEnd)).toList();
            case BookingState.REJECTED ->
                    allBookings.stream().filter(booking -> booking.getStatus().equals(BookingStatus.REJECTED))
                            .sorted(Comparator.comparing(Booking::getEnd)).toList();
            case BookingState.ALL -> allBookings;
        };
    }
}