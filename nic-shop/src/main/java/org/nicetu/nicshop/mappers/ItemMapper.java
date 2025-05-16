package org.nicetu.nicshop.mappers;

import org.nicetu.nicshop.dto.ItemDTO;
import org.nicetu.nicshop.dto.UserFeedbackDto;
import org.nicetu.nicshop.requests.admin.ItemRequest;
import org.nicetu.nicshop.security.jwt.JwtAuthentication;
import org.nicetu.nicshop.utils.ItemUtil;

import java.util.List;

public interface ItemMapper {
    static ItemDTO fromProductToDto(Item product, JwtAuthentication authentication,
                                    List<UserFeedbackDto> userFeedbackDtos) {
        ItemDTO productDto = new ItemDTO();
        productDto.setId(product.getId());
        productDto.setPictureUrl(product.getImage());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setStatus(ItemUtil.getStatus(product));
        productDto.setPrice(ItemUtil.getPrice(product, authentication));
        productDto.setUserFeedbackDtoList(userFeedbackDtos);

        return productDto;
    }


//    static List<ProductDto> fromProductsToDtos(List<Product> products, JwtAuthentication authentication) {
//        return products.stream()
//                .map(product -> ProductMapper.fromProductToDto(product, authentication))
//                .collect(Collectors.toList());
//    }

    static Item fromProductRequestToProduct(ItemRequest request, ItemProperty itemProperty, Category subcategory) {
        Item product = new Item();
        product.setId(request.getId());
//        product.setProductProperty(productProperty);
//        product.setPictureUrl(request.getPictureUrl());
//        product.setCategories(new ArrayList<Category>().add(subcategory));
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCount(Math.toIntExact(request.getAmount()));
        product.setPrice(request.getPrice());
        product.setDiscountPrice(request.getDiscountPrice());
        product.setDiscount(request.getDiscount());

        return product;
    }
}
