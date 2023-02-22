package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    @NotBlank
    private String name;

    @Column
    @NotBlank
    private String description;

    @Column(name = "is_available")
    private Boolean available;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "request_id")
    private Long requestId;

}
