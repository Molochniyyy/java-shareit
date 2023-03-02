package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoItem;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ItemDto implements Comparable<ItemDto> {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;

    private BookingDtoItem lastBooking;
    private BookingDtoItem nextBooking;
    private List<CommentDto> comments;

    @Override
    public int compareTo(ItemDto o) {
        return this.id.compareTo(o.id);
    }
}
