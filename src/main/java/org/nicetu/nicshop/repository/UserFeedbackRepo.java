package org.nicetu.nicshop.repository;

import org.nicetu.nicshop.domain.UserFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFeedbackRepo extends JpaRepository<UserFeedback, Long> {
}
