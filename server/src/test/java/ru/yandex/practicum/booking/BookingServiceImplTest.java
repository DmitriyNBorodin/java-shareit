package ru.yandex.practicum.booking;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.booking.dto.BookedItem;
import ru.yandex.practicum.booking.dto.BookingDto;
import ru.yandex.practicum.item.ItemRepository;
import ru.yandex.practicum.item.exceptions.ItemNotFoundException;
import ru.yandex.practicum.item.model.Item;
import ru.yandex.practicum.user.UserRepository;
import ru.yandex.practicum.user.exceptions.UserNotFoundException;
import ru.yandex.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {
    private BookingServiceImpl bookingService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Captor
    private ArgumentCaptor<Booking> bookingArgumentCaptor;

    @BeforeEach
    public void setUp() {
        bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
    }

    @Test
    void addNewBookingTest() {
        BookingDto newBookingDto = BookingDto.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(1))
                .itemId(1L)
                .bookerId(1L)
                .status(BookingStatus.WAITING)
                .build();
        Item item = new Item(1L, 2L, "name", "description", true, null);
        User user = new User(1L, "name", "1@mail.com");

        when(itemRepository.getItemById(Mockito.anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.getReferenceById(Mockito.anyLong())).thenReturn(item);
        when(userRepository.findUserById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(userRepository.getReferenceById(Mockito.anyLong())).thenReturn(user);

        bookingService.addNewBooking(1L, newBookingDto);

        verify(bookingRepository).save(bookingArgumentCaptor.capture());
        Booking savedBooking = bookingArgumentCaptor.getValue();
        assertEquals(BookingStatus.WAITING, savedBooking.getStatus());
        assertNotNull(savedBooking.getItem());
        assertNotNull(savedBooking.getBooker());
    }

    @Test
    void addNewBookingEmptyRequestTest() {
        BookingDto emptyDto = null;
        assertThrows(ValidationException.class, () -> bookingService.addNewBooking(1L, emptyDto));
    }

    @Test
    void addNewBookingUnknownUserTest() {
        BookingDto newBookingDto = BookingDto.builder().start(LocalDateTime.now()).end(LocalDateTime.now().plusHours(1))
                .itemId(1L).bookerId(1L).status(BookingStatus.WAITING).build();
        when(userRepository.findUserById(Mockito.anyLong())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> bookingService.addNewBooking(5L, newBookingDto));
    }

    @Test
    void addNewBookingUnknownItemTest() {
        BookingDto newBookingDto = BookingDto.builder().start(LocalDateTime.now()).end(LocalDateTime.now().plusHours(1))
                .itemId(1L).bookerId(1L).status(BookingStatus.WAITING).build();
        when(userRepository.findUserById(Mockito.anyLong())).thenReturn(Optional.of(new User()));
        when(itemRepository.getItemById(Mockito.anyLong())).thenReturn(Optional.empty());
        assertThrows(ItemNotFoundException.class, () -> bookingService.addNewBooking(1L, newBookingDto));
    }

    @Test
    void approveBookingTest() {
        Item item = new Item(1L, 1L, "name", "description", true, null);
        User user = new User(2L, "name", "1@mail.com");
        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(1))
                .item(item)
                .booker(user)
                .status(BookingStatus.WAITING)
                .build();
        BookedItem bookedItem = () -> new Item(1L, 1L, "name", "description", true, null);
        when(bookingRepository.findBookingById(Mockito.anyLong())).thenReturn(Optional.of(bookedItem));
        when(bookingRepository.getReferenceById(Mockito.anyLong())).thenReturn(booking);

        bookingService.approveBooking(1L, 1L, "true");
        verify(bookingRepository).save(bookingArgumentCaptor.capture());
        Booking savedBooking = bookingArgumentCaptor.getValue();
        assertEquals(BookingStatus.APPROVED, savedBooking.getStatus());
    }

    @Test
    void getBookingByIdTest() {
        Item item = new Item(1L, 1L, "name", "description", true, null);
        User booker = new User(1L, "name", "1@mail.com");
        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(1))
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();
        BookedItem bookedItem = () -> new Item(1L, 2L, "name", "description", true, null);
        when(bookingRepository.findBookingById(Mockito.anyLong())).thenReturn(Optional.of(bookedItem));
        when(bookingRepository.getReferenceById(Mockito.anyLong())).thenReturn(booking);

        bookingService.getBookingById(1L, 1L);
        verify(bookingRepository).findBookingById(Mockito.anyLong());
        verify(bookingRepository).getReferenceById(Mockito.anyLong());
    }

    @Test
    void getBookingsByUserIdTest() {
        User booker = new User(1L, "name", "1@mail.com");
        Booking booking1 = Booking.builder().id(1L).start(LocalDateTime.now()).end(LocalDateTime.now().plusHours(1))
                .item(new Item()).booker(booker).status(BookingStatus.WAITING).build();
        Booking booking2 = Booking.builder().id(2L).start(LocalDateTime.now().plusHours(3)).end(LocalDateTime.now().plusHours(4))
                .item(new Item()).booker(booker).status(BookingStatus.WAITING).build();
        Booking booking3 = Booking.builder().id(3L).start(LocalDateTime.now().plusHours(5)).end(LocalDateTime.now().plusHours(7))
                .item(new Item()).booker(booker).status(BookingStatus.REJECTED).build();

        when(bookingRepository.findBookingByBookerId(1L)).thenReturn(List.of(booking1, booking2, booking3));

        List<Booking> waitingList = bookingService.getBookingsByUserId(1L, "WAITING");

        assertEquals(2, waitingList.size());
        verify(bookingRepository, atLeast(1)).findBookingByBookerId(1L);
    }

    @Test
    void getBookingsByOwnerIdFailedTest() {
        when(userRepository.findUserById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> bookingService.getBookingsByOwnerId(1L, "ALL"));
        verify(bookingRepository, never()).findBookingByOwnerId(Mockito.anyLong());
    }

    @Test
    void getFutureBookingsByOwnerIdTest() {
        Booking newBookingDto1 = Booking.builder().start(LocalDateTime.now().plusHours(2)).end(LocalDateTime.now().plusHours(3))
                .item(new Item()).booker(new User()).status(BookingStatus.WAITING).build();
        Booking newBookingDto2 = Booking.builder().start(LocalDateTime.now().plusHours(6)).end(LocalDateTime.now().plusHours(7))
                .item(new Item()).booker(new User()).status(BookingStatus.WAITING).build();
        when(userRepository.findUserById(Mockito.anyLong())).thenReturn(Optional.of(new User()));
        when(bookingRepository.findBookingByOwnerId(Mockito.anyLong()))
                .thenReturn(new ArrayList<>(Arrays.asList(newBookingDto1, newBookingDto2)));

        List<Booking> futureBookings = bookingService.getBookingsByOwnerId(1L, "FUTURE");

        assertEquals(2, futureBookings.size());
    }
}
