package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Comment;

import java.util.Set;

public interface CommentsRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment as c join c.item as i where i.id = ?1")
    public Set<Comment> getCommentsByItemId(Long itemId);
}
