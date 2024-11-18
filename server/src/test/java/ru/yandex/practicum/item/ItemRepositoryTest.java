package ru.yandex.practicum.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.item.dto.ItemForRequestDao;
import ru.yandex.practicum.item.model.Item;
import ru.yandex.practicum.request.ItemRequest;
import ru.yandex.practicum.request.ItemRequestRepository;
import ru.yandex.practicum.user.UserRepository;
import ru.yandex.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Test
    void findItemByOwnerId() {
        User testOwner = User.builder().name("name").email("1@mail.com").build();
        Item item1 = Item.builder().ownerId(1L).name("itemName1").description("itemDescription1").available(true).build();
        Item item2 = Item.builder().ownerId(1L).name("itemName2").description("itemDescription2").available(true).build();
        userRepository.save(testOwner);
        itemRepository.save(item1);
        itemRepository.save(item2);

        List<Item> owner1Items = itemRepository.findItemByOwnerId(1L);

        assertEquals(2, owner1Items.size());
    }

    @Test
    void getByNameContainingOrDescriptionContainingAllIgnoreCase() {
        User testOwner = User.builder().name("name").email("1@mail.com").build();
        Item item1 = Item.builder().ownerId(1L).name("itemName1").description("itemDescription1").available(true).build();
        Item item2 = Item.builder().ownerId(1L).name("itemName2").description("itemDescription2").available(true).build();
        userRepository.save(testOwner);
        itemRepository.save(item1);
        itemRepository.save(item2);

        List<Item> searchedItems = itemRepository.getByNameContainingOrDescriptionContainingAllIgnoreCase("DESCR", "DESCR");

        assertEquals(2, searchedItems.size());
    }

    @Test
    void getItemById() {
        User testUser = User.builder().name("name").email("1@mail.com").build();
        Item testItem = Item.builder().ownerId(1L).name("itemName").description("itemDescription").available(true).build();
        userRepository.save(testUser);
        itemRepository.save(testItem);

        Optional<Item> requestedItem = itemRepository.getItemById(1L);

        assertTrue(requestedItem.isPresent());
        assertEquals("itemName", requestedItem.get().getName());
    }

    @Test
    void findByRequestId() {
        User testUser1 = User.builder().name("name1").email("1@mail.com").build();
        User testUser2 = User.builder().name("name2").email("2@mail.com").build();
        Item testItem = Item.builder().ownerId(2L).name("itemName").description("itemDescription")
                .available(true).requestId(1L).build();
        ItemRequest itemRequest = ItemRequest.builder().description("anything").userId(1L)
                .created(LocalDateTime.now()).build();

        userRepository.save(testUser1);
        userRepository.save(testUser2);
        itemRequestRepository.save(itemRequest);
        itemRepository.save(testItem);

        List<ItemForRequestDao> itemsByRequest = itemRepository.findByRequestId(1L);

        assertFalse(itemsByRequest.isEmpty());
        assertEquals(2L, itemsByRequest.getFirst().getOwnerId());
    }
}