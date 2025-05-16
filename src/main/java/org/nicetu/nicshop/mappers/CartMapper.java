package org.nicetu.nicshop.mappers;


import org.nicetu.nicshop.dto.BucketDTO;
import org.nicetu.nicshop.dto.BucketItemDTO;

import java.util.List;
import java.util.stream.LongStream;

public interface CartMapper {
    static BucketDTO fromUserProductDTOListToCartDto(List<BucketItemDTO> bucketItemDTOList) {
        BucketDTO bucketDTO = new BucketDTO();
        bucketDTO.setItems(bucketItemDTOList);
        bucketDTO.setCount(bucketItemDTOList.stream()
                .flatMapToLong(a -> LongStream.of(a.getAmount()))
                .sum());
        bucketDTO.setFullPrice(bucketItemDTOList.stream()
                .flatMapToLong(p -> LongStream.of(p.getPrice() * p.getAmount()))
                .sum());

        return bucketDTO;
    }
}