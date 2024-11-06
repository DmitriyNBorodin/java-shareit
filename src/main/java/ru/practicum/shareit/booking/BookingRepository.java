package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.dto.BookedItem;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<BookedItem> findBookingById(Long bookingId);

    @Query("select b from Booking as b join b.booker as boo where boo.id = ?1")
    List<Booking> findBookingByBookerId(Long bookerId);

    @Query("select b from Booking as b join b.item as i where i.ownerId = ?1")
    List<Booking> findBookingByOwnerId(Long ownerId);

    @Query("select b from Booking as b join b.item as i join b.booker as boo where i.id = ?1 and boo.id = ?2")
    Optional<Booking> findBookingByItemIdAndBookerId(Long itemId, Long bookerId);

    @Query("select b from Booking as b join b.item as i where i.id = ?1")
    List<Booking> findBookingByItemId(Long itemId);
}
