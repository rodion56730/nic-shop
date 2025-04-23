package org.nicetu.nicshop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "buckets_items")
@Getter
@Setter
public class BucketItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bucket_id")
    private Bucket bucket;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private int quantity; // Количество товара
    private LocalDateTime addedAt; // Когда добавлен
}
