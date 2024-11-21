package ru.yandex.practicum.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.item.dto.ItemForRequestDao;
import ru.yandex.practicum.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findItemByOwnerId(Long ownerId);

    List<Item> getByNameContainingOrDescriptionContainingAllIgnoreCase(String nameSearch, String descriptionSearch);

    Optional<Item> getItemById(Long itemId);

    List<ItemForRequestDao> findByRequestId(Long requestId);

    @Query("select id from Item as i where i.requestId in ?1")
    List<ItemForRequestDao> findByRequestId(List<Long> requestId);
}
