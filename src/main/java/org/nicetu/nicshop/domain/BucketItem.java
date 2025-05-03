package org.nicetu.nicshop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_product")
@Getter
@Setter
@NoArgsConstructor
public class BucketItem {
    @Id
    @Column(name = "user_product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Item product;

    @Column(name = "amount")
    private Long amount;

    private String name;
}
