package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder(toBuilder = true)
@AllArgsConstructor
public class CommentDto {
    private Long id;
    @NotNull
    private String text;
    private String authorName;
    private LocalDateTime created;
}
