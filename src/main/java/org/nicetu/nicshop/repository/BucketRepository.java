package org.nicetu.nicshop.repository;

import org.nicetu.nicshop.domain.Bucket;
import org.nicetu.nicshop.domain.BucketItem;
import org.nicetu.nicshop.domain.Item;
import org.nicetu.nicshop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface BucketRepository extends JpaRepository<Bucket, Long> {
    Optional<Bucket> findByUserId(Long userId);
    List<Bucket> findAllByUser(User user);

    void deleteAllByUser(User user);
}
