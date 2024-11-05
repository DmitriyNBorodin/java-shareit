package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("select b.item from Booking as b where b.id = ?1")
    public Item getItemById(Long bookingId);

    @Query("select b from Booking as b join b.booker as boo where boo.id = ?1")
    public List<Booking> findBookingByBookerId(Long bookerId);

    @Query("select b from Booking as b join b.item as i where i.ownerId = ?1")
    public List<Booking> findBookingByOwnerId(Long ownerId);

    @Query("select b from Booking as b join b.item as i join b.booker as boo where i.id = ?1 and boo.id = ?2")
    public Optional<Booking> findBookingByItemIdAndBookerId(Long itemId, Long bookerId);

    @Query("select b from Booking as b join b.item as i where i.id = ?1")
    public List<Booking> findBookingByItemId(Long itemId);
}
