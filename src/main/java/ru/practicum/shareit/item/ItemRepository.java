package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final Map<Long, Item> itemStorage;

    public Item addItem(Item item) {
        item.setItemId(generateNewId());
        itemStorage.put(item.getItemId(), item);
        return item;
    }

    public Item getItemById(Long itemId) {
        return itemStorage.get(itemId);
    }

    public List<Item> getItemsByOwnerId(Long ownerId) {
        System.out.println(itemStorage.size());
        return itemStorage.values().stream().filter(item -> item.getOwnerId().equals(ownerId)).toList();
    }

    public List<Item> searchForItems(String text) {
        String textForSearch = text.toLowerCase();
        return itemStorage.values().stream().filter(item -> item.getName().toLowerCase().contains(textForSearch) ||
                                                            item.getDescription().toLowerCase().contains(textForSearch))
                .filter(Item::getAvailable)
                .toList();
    }

    private Long generateNewId() {
        if (!itemStorage.isEmpty()) {
            return itemStorage.keySet().stream().max(Long::compare).get() + 1;
        } else return 1L;
    }
}
