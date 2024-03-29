package ru.practicum.shareit.item.validator;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;

@Slf4j
public class ItemValidator {
    public static void isValidCreateItem(ItemDto item) {
        if (item.getName() == null || item.getAvailable() == null || item.getDescription() == null
                || item.getDescription().isEmpty() || item.getName().isEmpty()) {
            log.warn("Имя, описание и статус - обязательны к заполнению");
            throw new ValidationException("Имя, описания и статус - обязательны к заполнению");
        }

    }
}
