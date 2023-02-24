package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserWrapper;

public class BookingWrapper {
    public static BookingDto toBookingDto(Booking booking, Item item, User user) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(item)
                .booker(UserWrapper.toUserDto(user))
                .status(booking.getStatus())
                .build();
    }

    public static BookingDtoItem toBookingDtoItem(Booking booking) {
        return BookingDtoItem.builder()
                .id(booking.getId())
                .bookerId(booking.getBookerId())
                .build();
    }
}
