package org.nicetu.nicshop.service.api.admin;

import org.nicetu.nicshop.requests.DeleteFeedbackRequest;

public interface FeedbackAdminService {

    void deleteFeedback(DeleteFeedbackRequest request, Long id);
    void deletePhotosFeedback(DeleteFeedbackRequest request, Long id);
    void deleteCommentFeedback(DeleteFeedbackRequest request, Long id);

}
