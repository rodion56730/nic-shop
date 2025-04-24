package org.nicetu.nicshop.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BucketDTO {
    private Long id;
    private Long userId;
    private List<ItemDTO> items;
    private BigDecimal totalPrice;
}
