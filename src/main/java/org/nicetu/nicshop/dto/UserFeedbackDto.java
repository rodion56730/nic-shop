package org.nicetu.nicshop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserFeedbackDto {
    private List<String> picturesUrls;

    private String comment;

    private Long feedback;
}
