package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.dto.BookingDtoItem;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
public class ItemDto implements Comparable<ItemDto> {
    private Long id;
    @NotBlank(groups = {Create.class})
    private String name;
    @NotBlank(groups = {Create.class})
    private String description;
    @NotNull(groups = {Create.class})
    private Boolean available;
    private Long requestId;

    private BookingDtoItem lastBooking;
    private BookingDtoItem nextBooking;
    private Collection<CommentDto> comments;

    @Override
    public int compareTo(ItemDto o) {
        return this.id.compareTo(o.id);
    }
}
