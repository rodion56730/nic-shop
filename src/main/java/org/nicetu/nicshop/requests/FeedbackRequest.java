package org.nicetu.nicshop.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FeedbackRequest {
    @NotNull
    private List<String> picturesUrls;

    private String comment;

    @NotNull
    @Min(1)
    @Max(5)
    private Long feedback;

}
