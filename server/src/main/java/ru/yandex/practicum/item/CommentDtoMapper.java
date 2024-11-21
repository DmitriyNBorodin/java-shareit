package ru.yandex.practicum.item;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.booking.Booking;
import ru.yandex.practicum.item.dto.CommentDto;
import ru.yandex.practicum.item.model.Comment;

import java.time.LocalDateTime;

@Component
public class CommentDtoMapper {
    public Comment toComment(CommentDto commentDto, Booking bookingForComment) {
        return Comment.builder()
                .text(commentDto.getText())
                .author(bookingForComment.getBooker())
                .item(bookingForComment.getItem())
                .created(LocalDateTime.now())
                .build();
    }

    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }
}
