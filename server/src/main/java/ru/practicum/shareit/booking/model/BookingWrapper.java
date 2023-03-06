package ru.practicum.shareit.booking.model;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemWrapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserWrapper;


@UtilityClass
public class  BookingWrapper {

    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(ItemWrapper.toItemDto(booking.getItem()))
                .booker(UserWrapper.toUserDto(booking.getBooker()))
                .status(booking.getStatus())
                .build();
    }

    public static BookingDtoItem toBookingDtoItem(Booking booking) {
        if (booking == null) {
            return null;
        }
        return BookingDtoItem.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .bookerId(booking.getBooker().getId())
                .status(booking.getStatus())
                .build();
    }

    public static Booking toBooking(BookingDtoRequest bookingDtoRequest, Item item, User booker) {
        return Booking.builder()
                .start(bookingDtoRequest.getStart())
                .end(bookingDtoRequest.getEnd())
                .item(item)
                .status(BookingStatus.WAITING)
                .booker(booker)
                .build();
    }
}

