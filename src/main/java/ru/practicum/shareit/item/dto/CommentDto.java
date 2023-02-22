package ru.practicum.shareit.item.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class CommentDto {

    private Long id;

    private String text;

    private Long itemId;

    private Long authorId;

    private LocalDateTime created;
}
