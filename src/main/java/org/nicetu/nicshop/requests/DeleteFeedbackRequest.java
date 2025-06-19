package org.nicetu.nicshop.requests;

;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
public class DeleteFeedbackRequest {
    @NotNull
    @Schema(description = "Id отзыва на сайте")
    private Long feedbackId;
}
