package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    Collection<ItemRequest> findByRequesterIdIsOrderByCreatedDesc(Long requesterId);

    Page<ItemRequest> findAllByRequesterIdIsNotOrderByCreatedDesc(Long requesterId, Pageable pageable);
}
