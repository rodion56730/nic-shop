package org.nicetu.nicshop.mappers;



import org.nicetu.nicshop.domain.BucketItem;
import org.nicetu.nicshop.dto.BucketItemDTO;
import org.nicetu.nicshop.security.jwt.JwtAuthentication;
import org.nicetu.nicshop.utils.ProductUtil;

import java.util.List;
import java.util.stream.Collectors;

public interface BucketItemMapper {
    static BucketItemDTO fromUserProductToDto(BucketItem userProduct, JwtAuthentication authentication) {
        BucketItemDTO userProductDto = new BucketItemDTO();
        userProductDto.setPictureUrl(userProduct.getProduct().getImage());
        userProductDto.setName(userProduct.getProduct().getName());
        userProductDto.setPrice(ProductUtil.getPrice(userProduct.getProduct(), authentication));
        userProductDto.setAmount(userProduct.getAmount());

        return userProductDto;
    }

    static List<BucketItemDTO> fromUserProductsToDtos(List<BucketItem> userProducts, JwtAuthentication authentication) {
        return userProducts.stream()
                .map(userProduct -> BucketItemMapper.fromUserProductToDto(userProduct, authentication))
                .collect(Collectors.toList());
    }
}
