package org.nicetu.nicshop.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.nicetu.nicshop.requests.DeleteFeedbackRequest;
import org.nicetu.nicshop.service.api.admin.FeedbackAdminService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Отзывы")
@RestController
@RequestMapping("/api/admin/feedback")
@PreAuthorize("hasAuthority('ADMIN')")
public class FeedbackAdminController {

    private final FeedbackAdminService feedbackService;

    public FeedbackAdminController(FeedbackAdminService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @DeleteMapping("/product/{id}")
    public void deleteFeedback(@Valid @RequestBody DeleteFeedbackRequest request, @PathVariable Long id) {
        feedbackService.deleteFeedback(request, id);
    }

    @DeleteMapping("/product/{id}/photos")
    public void deletePhotosFeedback(@Valid @RequestBody DeleteFeedbackRequest request, @PathVariable Long id) {
        feedbackService.deletePhotosFeedback(request, id);
    }

    @PutMapping("/product/{id}/comment")
    public void deleteCommentFeedback(@Valid @RequestBody DeleteFeedbackRequest request, @PathVariable Long id) {
        feedbackService.deleteCommentFeedback(request, id);
    }
}

