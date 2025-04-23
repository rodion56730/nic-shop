package org.nicetu.nicshop.repository;

import org.nicetu.nicshop.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
