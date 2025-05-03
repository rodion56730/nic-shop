package org.nicetu.nicshop.repository;

import org.nicetu.nicshop.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface ItemRepository extends JpaRepository<Item, Long> {

}
