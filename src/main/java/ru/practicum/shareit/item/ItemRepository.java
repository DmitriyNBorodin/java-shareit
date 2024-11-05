package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    public List<Item> findItemByOwnerId(Long ownerId);

    public List<Item> getByNameContainingOrDescriptionContainingAllIgnoreCase(String nameSearch, String descriptionSearch);

    public Optional<Item> getItemById(Long itemId);
}
