package ru.yandex.practicum.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest,Long> {

    List<ItemRequest> findItemRequestByUserId(Long userId);

    Optional<ItemRequest> findItemRequestById(Long itemRequestId);
}
