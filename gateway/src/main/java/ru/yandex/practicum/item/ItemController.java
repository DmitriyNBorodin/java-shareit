package ru.yandex.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.util.Marker;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addNewItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestBody @Validated(Marker.OnCreate.class) ItemDto newItem) {
        return itemClient.addNewItem(userId, newItem);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addNewComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestBody @Validated CommentDto comment, @PathVariable Long itemId) {
        return itemClient.addNewItemComment(userId, itemId, comment);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestBody @Validated(Marker.OnUpdate.class) ItemDto itemToUpdate, @PathVariable Long itemId) {
        return itemClient.updateItem(userId, itemToUpdate, itemId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable Long itemId) {
        return itemClient.getItemById(itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByOwnerId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.getItemsByOwnerId(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchForItem(@RequestParam String text) {
        return itemClient.searchForItems(text);
    }
}