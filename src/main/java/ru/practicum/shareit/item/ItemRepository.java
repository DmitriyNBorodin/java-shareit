package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findItemByOwnerId(Long ownerId);

    List<Item> getByNameContainingOrDescriptionContainingAllIgnoreCase(String nameSearch, String descriptionSearch);

    Optional<Item> getItemById(Long itemId);
}
