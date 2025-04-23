package org.nicetu.nicshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BucketItemDTO {
    private Long itemId;
    private String itemName;
    private BigDecimal itemPrice;
    private int quantity;
    private BigDecimal totalPrice;
}