package org.nicetu.nicshop.service;

import lombok.RequiredArgsConstructor;
import org.nicetu.nicshop.domain.Bucket;
import org.nicetu.nicshop.domain.Item;
import org.nicetu.nicshop.domain.User;
import org.nicetu.nicshop.dto.BucketDTO;
import org.nicetu.nicshop.dto.ItemDTO;
import org.nicetu.nicshop.repository.BucketRepository;
import org.nicetu.nicshop.repository.ItemRepository;
import org.nicetu.nicshop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BucketService {
    private final BucketRepository bucketRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public BucketDTO getBucketByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));

        Bucket bucket = bucketRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Bucket newBucket = new Bucket();
                    newBucket.setUser(user);
                    return bucketRepository.save(newBucket);
                });

        return BucketDTO.builder()
                .items(bucket.getItems().stream()
                        .map(item -> new ItemDTO(item.getId(),item.getImage(), item.getTitle(),item.getDescription(), item.getStatus(),item.getPrice(),item.getUserFeedbacks()))
                        .collect(Collectors.toList()).reversed())
                .totalPrice(calculateTotalPrice(bucket))
                .build();
    }

    public void addItemToBucket(Long userId, Long itemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Item not found with id: " + itemId));

        Bucket bucket = bucketRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Bucket newBucket = new Bucket();
                    newBucket.setUser(user);
                    return bucketRepository.save(newBucket);
                });

        if (!bucket.getItems().contains(item)) {
            bucket.getItems().add(item);
            bucketRepository.save(bucket);
        }
    }

    public void removeItemFromBucket(Long userId, Long itemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));

        Bucket bucket = bucketRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NoSuchElementException("Bucket not found for user with id: " + userId));

        Item itemToRemove = bucket.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Item not found in bucket with id: " + itemId));

        bucket.getItems().remove(itemToRemove);
        bucketRepository.save(bucket);
    }

    public void clearBucket(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));

        Bucket bucket = bucketRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NoSuchElementException("Bucket not found for user with id: " + userId));

        bucket.getItems().clear();
        bucketRepository.save(bucket);
    }

    private BigDecimal calculateTotalPrice(Bucket bucket) {
        return bucket.getItems().stream()
                .map(Item::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
