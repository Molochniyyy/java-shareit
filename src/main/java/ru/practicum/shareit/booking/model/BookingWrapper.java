package ru.practicum.shareit.booking.model;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemWrapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserWrapper;

@RequiredArgsConstructor
public class BookingWrapper {

    public static BookingDto toBookingDto(Booking booking, Item item, User booker) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(ItemWrapper.toItemDto(item))
                .booker(UserWrapper.toUserDto(booker))
                .status(booking.getStatus())
                .build();
    }

    public static BookingDtoItem toBookingDtoItem(Booking booking) {
        return BookingDtoItem.builder()
                .id(booking.getId())
                .bookerId(booking.getBookerId())
                .build();
    }

    public static Booking toBooking(BookingDtoRequest bookingDtoRequest, Item item, User booker) {
        return Booking.builder()
                .start(bookingDtoRequest.getStart())
                .end(bookingDtoRequest.getEnd())
                .itemId(item.getId())
                .bookerId(booker.getId())
                .build();
    }

    public static BookingDto toBookingDtoForUpdate(Booking booking, Item item, User booker) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(ItemWrapper.toItemDto(item))
                .booker(UserWrapper.toUserDto(booker))
                .status(booking.getStatus())
                .build();
    }


}

