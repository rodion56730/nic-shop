package org.nicetu.nicshop.mappers;


import org.nicetu.nicshop.domain.User;
import org.nicetu.nicshop.dto.BucketItemDTO;
import org.nicetu.nicshop.dto.CatalogDTO;
import org.nicetu.nicshop.dto.ItemDTO;
import org.nicetu.nicshop.utils.CatalogUtil;

import java.util.List;
import java.util.stream.LongStream;

public interface CatalogMapper {
    static CatalogDTO fromProductDtosToCatalogDto(List<ItemDTO> productDtos, List<BucketItemDTO> userProductDtos,
                                                  User user) {
        CatalogDTO catalogDto = new CatalogDTO();

        catalogDto.setInitials(CatalogUtil.getInitials(user));
        catalogDto.setProductDtos(productDtos);
        catalogDto.setCategoryCount((long) productDtos.size());
        catalogDto.setCartCount(userProductDtos.stream()
                .flatMapToLong(a -> LongStream.of(a.getAmount()))
                .sum());

        return catalogDto;
    }
}
