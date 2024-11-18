package ru.yandex.practicum.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.booking.Booking;
import ru.yandex.practicum.booking.BookingStatus;
import ru.yandex.practicum.item.dto.CommentDto;
import ru.yandex.practicum.item.model.Comment;
import ru.yandex.practicum.item.model.Item;
import ru.yandex.practicum.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentDtoMapperTest {
    @Autowired
    private CommentDtoMapper commentDtoMapper;

    @Test
    void toCommentTest() {
        Booking booking = new Booking(1L, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusHours(12),
                new Item(), new User(), BookingStatus.APPROVED);
        CommentDto commentDto = new CommentDto(1L, "anything", null, null);
        Comment comment = commentDtoMapper.toComment(commentDto, booking);

        assertNotNull(comment);
        assertEquals("anything", comment.getText());
    }

    @Test
    void toCommentDtoTest() {
        User user = new User(1L, "name", "1@mail.com");
        Comment comment = new Comment(1L, "anything", new Item(), user, LocalDateTime.now());

        CommentDto commentDto = commentDtoMapper.toCommentDto(comment);

        assertNotNull(commentDto);
        assertEquals("name", commentDto.getAuthorName());
        assertEquals("anything", commentDto.getText());
    }
}