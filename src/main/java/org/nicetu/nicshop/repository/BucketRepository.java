package org.nicetu.nicshop.repository;

import org.nicetu.nicshop.domain.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BucketRepository extends JpaRepository<Bucket, Long> {
    Optional<Bucket> findByUserId(Long userId);
}
