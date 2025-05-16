package org.nicetu.nicshop.repository;

import org.nicetu.nicshop.domain.ItemProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemPropertyRepository extends JpaRepository<ItemProperty, Long> {
}
