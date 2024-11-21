package ru.yandex.practicum.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.user.UserRepository;
import ru.yandex.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ItemRequestRepositoryTest {
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void findItemRequestByUserIdTest() {
        User testUser = User.builder().name("name").email("1@mail.com").build();
        ItemRequest itemRequest1 = ItemRequest.builder().description("anything").userId(1L)
                .created(LocalDateTime.now()).build();
        ItemRequest itemRequest2 = ItemRequest.builder().description("anythingMore").userId(1L)
                .created(LocalDateTime.now()).build();

        userRepository.save(testUser);
        itemRequestRepository.save(itemRequest1);
        itemRequestRepository.save(itemRequest2);

        List<ItemRequest> allUserRequests = itemRequestRepository.findItemRequestByUserId(1L);

        assertEquals(2, allUserRequests.size());
        assertEquals("anything", allUserRequests.getFirst().getDescription());
    }

    @Test
    public void findItemRequestByIdTest() {
        User testUser = User.builder().name("name").email("1@mail.com").build();
        ItemRequest itemRequest = ItemRequest.builder().description("anything").userId(1L)
                .created(LocalDateTime.now()).build();

        userRepository.save(testUser);
        itemRequestRepository.save(itemRequest);

        Optional<ItemRequest> expectedRequest = itemRequestRepository.findItemRequestById(1L);
        assertTrue(expectedRequest.isPresent());
        assertEquals("anything", expectedRequest.get().getDescription());
    }
}