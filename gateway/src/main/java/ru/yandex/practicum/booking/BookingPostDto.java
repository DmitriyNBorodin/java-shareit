package ru.yandex.practicum.booking;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BookingPostDto {
    @NotNull(message = "ID вещи для бронирование должен быть указан")
    private long itemId;
    @FutureOrPresent(message = "Начало бронирования не может быть в прошлом")
    @NotNull(message = "Время начала бронирования должно быть указано")
    private LocalDateTime start;
    @Future(message = "Окончание бронирования должно быть в будущем")
    @NotNull(message = "Время окончания бронирования должно быть указано")
    private LocalDateTime end;
}
