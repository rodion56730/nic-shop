package org.nicetu.nicshop.repository;


import org.nicetu.nicshop.domain.Photo;
import org.nicetu.nicshop.domain.UserFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepo extends JpaRepository<Photo, Long> {
    public List<Photo> getAllByUserFeedback(UserFeedback userFeedback);
}
