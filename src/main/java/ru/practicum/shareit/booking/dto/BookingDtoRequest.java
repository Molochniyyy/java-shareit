package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.Create;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDtoRequest {
    @Positive(groups = {Create.class})
    @NotNull(groups = {Create.class})
    private Long itemId;
    @FutureOrPresent(groups = {Create.class})
    @NotNull(groups = {Create.class})
    private LocalDateTime start;
    @Future(groups = {Create.class})
    @NotNull(groups = {Create.class})
    private LocalDateTime end;
}
