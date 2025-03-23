package ru.yandex.practicum.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.item.model.Comment;

import java.util.Set;

public interface CommentsRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment as c join c.item as i where i.id = ?1")
    Set<Comment> getCommentsByItemId(Long itemId);
}
