package org.nicetu.nicshop.service;

import lombok.RequiredArgsConstructor;
import org.nicetu.nicshop.domain.Bucket;
import org.nicetu.nicshop.dto.BucketDTO;
import org.nicetu.nicshop.repository.BucketRepository;
import org.nicetu.nicshop.repository.ItemRepository;
import org.nicetu.nicshop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BucketService {
    private final BucketRepository bucketRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public BucketDTO getBucketByUserId(Long userId) {
        // Логика получения корзины
    }

    public void addItemToBucket(Long userId, Long itemId) {
        // Логика добавления товара
    }

    public void removeItemFromBucket(Long userId, Long itemId) {
        // Логика удаления товара
    }

    public void clearBucket(Long userId) {
        // Логика очистки корзины
    }

    private BigDecimal calculateTotalPrice(Bucket bucket) {
        // Логика расчета общей суммы
    }
}
