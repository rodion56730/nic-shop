package org.nicetu.nicshop.controller;

import lombok.RequiredArgsConstructor;
import org.nicetu.nicshop.dto.BucketDTO;
import org.nicetu.nicshop.service.BucketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/buckets")
@RequiredArgsConstructor
public class BucketController {
    private final BucketService bucketService;

    @GetMapping("/{userId}")
    public ResponseEntity<BucketDTO> getBucket(@PathVariable Long userId) {
        return ResponseEntity.ok(bucketService.getBucketByUserId(userId));
    }

    @PostMapping("/{userId}/items/{itemId}")
    public ResponseEntity<Void> addItem(
            @PathVariable Long userId,
            @PathVariable Long itemId
    ) {
        bucketService.addItemToBucket(userId, itemId);
        return ResponseEntity.ok().build();
    }

    // Другие методы
}
