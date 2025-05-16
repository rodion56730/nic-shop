package org.nicetu.nicshop.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    public List<Photo> getAllByUserFeedback(UserFeedback userFeedback);
}
