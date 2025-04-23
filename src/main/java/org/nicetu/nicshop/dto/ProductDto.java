package org.nicetu.nicshop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.nicetu.nicshop.domain.ProductStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductDto {
    private Long id;

    private String pictureUrl;

    private String title;

    private String description;

    private ProductStatus status;

    private Long price;

    private List<UserFeedbackDto> userFeedbackDtoList;
}

