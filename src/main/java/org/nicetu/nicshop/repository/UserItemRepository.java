package org.nicetu.nicshop.repository;

import org.nicetu.nicshop.domain.BucketItem;
import org.nicetu.nicshop.domain.Item;
import org.nicetu.nicshop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserItemRepository extends JpaRepository<BucketItem, Long> {

    List<BucketItem> findAllByUser(User user);

    void deleteAllByUser(User user);

    Optional<BucketItem> findByItemAndUser(Item item, User user);

}
