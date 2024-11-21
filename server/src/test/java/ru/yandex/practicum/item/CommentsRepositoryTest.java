package ru.yandex.practicum.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.yandex.practicum.item.model.Comment;
import ru.yandex.practicum.item.model.Item;
import ru.yandex.practicum.user.UserRepository;
import ru.yandex.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentsRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CommentsRepository commentsRepository;

    @Test
    void getCommentsByItemId() {
        User testOwner = User.builder().name("name1").email("1@mail.com").build();
        User testUser = User.builder().name("name2").email("2@mail.com").build();
        User savedTestOwner = userRepository.save(testOwner);
        User savedTestUser = userRepository.save(testUser);

        Item testItem = Item.builder().ownerId(savedTestOwner.getId()).name("itemName").description("itemDescription").available(true).build();
        Comment comment = Comment.builder().author(savedTestUser).item(testItem).text("anyText").created(LocalDateTime.now())
                .build();

        Item savedTestItem = itemRepository.save(testItem);
        commentsRepository.save(comment);

        Set<Comment> itemComments = commentsRepository.getCommentsByItemId(savedTestItem.getId());

        assertFalse(itemComments.isEmpty());
        assertTrue(itemComments.contains(comment));
    }
}