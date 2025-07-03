package org.nicetu.nicshop.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
