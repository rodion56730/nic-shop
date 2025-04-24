package org.nicetu.nicshop.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.nicetu.nicshop.domain.ProductStatus;
import org.nicetu.nicshop.domain.UserFeedback;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
    private Long id;

    private String pictureUrl;

    private String title;

    private String description;


    private ProductStatus status;

    private Long price;

    private List<UserFeedbackDto> userFeedbackDtoList;

    public ItemDTO(Long id, String image, String title, String description, ProductStatus status, BigDecimal price, List<UserFeedback> userFeedbacks) {
    }
}
