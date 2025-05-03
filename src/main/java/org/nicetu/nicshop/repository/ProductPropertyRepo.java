package org.nicetu.nicshop.repository;

import org.nicetu.nicshop.domain.ProductProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPropertyRepo extends JpaRepository<ProductProperty, Long> {
}
