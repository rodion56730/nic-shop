package org.nicetu.nicshop.service.impl.admin;

import org.nicetu.nicshop.domain.UserFeedback;
import org.nicetu.nicshop.repository.*;
import org.nicetu.nicshop.requests.DeleteFeedbackRequest;
import org.nicetu.nicshop.service.api.admin.FeedbackAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FeedbackAdminServiceImpl implements FeedbackAdminService {

    private final ItemRepository productRepo;
    private final PhotoRepository photoRepository;
    private final UserFeedbackRepository userFeedbackRepository;

    @Autowired
    public FeedbackAdminServiceImpl(ItemRepository productRepo,
                                    PhotoRepository photoRepository,
                                    UserFeedbackRepository userFeedbackRepository
    ) {
        this.productRepo = productRepo;
        this.photoRepository = photoRepository;
        this.userFeedbackRepository = userFeedbackRepository;
    }

    @Transactional
    public void deleteFeedback(DeleteFeedbackRequest request, Long id) {
        productRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не найден"));
        userFeedbackRepository.delete(userFeedbackRepository.findById(request.getFeedbackId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Отзыв не найден")));
    }

    @Transactional
    public void deletePhotosFeedback(DeleteFeedbackRequest request, Long id) {
        productRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не найден"));
        UserFeedback feedback = userFeedbackRepository.findById(request.getFeedbackId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Отзыв не найден"));
        photoRepository.deleteAll(photoRepository.getAllByUserFeedback(feedback));
    }

    @Transactional
    public void deleteCommentFeedback(DeleteFeedbackRequest request, Long id) {
        productRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не найден"));
        UserFeedback feedback = userFeedbackRepository.findById(request.getFeedbackId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Отзыв не найден"));
        feedback.setComment(null);
        userFeedbackRepository.save(feedback);
    }
}
