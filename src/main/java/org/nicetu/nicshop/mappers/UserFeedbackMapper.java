package org.nicetu.nicshop.mappers;

import org.nicetu.nicshop.domain.Photo;
import org.nicetu.nicshop.domain.UserFeedback;
import org.nicetu.nicshop.dto.UserFeedbackDto;
import java.util.List;
import java.util.stream.Collectors;

public interface UserFeedbackMapper {

    static UserFeedbackDto fromUserFeedbackToDto(UserFeedback userFeedback) {
        UserFeedbackDto userFeedbackDto = new UserFeedbackDto();
        userFeedbackDto.setFeedback(userFeedback.getFeedback());
        userFeedbackDto.setComment(userFeedback.getComment());
        userFeedbackDto.setPicturesUrls(userFeedback.getPhotos().stream()
                .map(Photo::getPictureUrl)
                .collect(Collectors.toList()));
        return userFeedbackDto;
    }

    static List<UserFeedbackDto> fromUserFeedbacksToDtos(List<UserFeedback> userFeedbacks) {
        return userFeedbacks.stream()
                .map(UserFeedbackMapper::fromUserFeedbackToDto)
                .collect(Collectors.toList());
    }

}
