package org.nicetu.nicshop.domain;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
public class Item {

    @Id
    private Long id;
    private String name;
    private String description;
    private Long price;
    private String image;
    private int count;
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "items_categories",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<UserFeedback> userFeedbacks = new ArrayList<>();

    @Column(name = "discount")
    private Boolean discount;
    @Column(name = "discount_price")
    private Long discountPrice;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_property_id")
    private ItemProperty itemProperty;

}
