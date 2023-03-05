package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findAllByOwnerIsOrderByIdAsc(User owner, Pageable pageable);

    @Query("SELECT i FROM Item as i " +
            "WHERE (upper(i.name) like upper(CONCAT('%', ?1, '%')) " +
            "or upper(i.description) like upper(CONCAT('%', ?1, '%')))" +
            "and i.available = true")
    Page<Item> searchItems(String text, Pageable pageable);

    Collection<Item> findAllByRequestIn(Collection<ItemRequest> itemRequests);
}
