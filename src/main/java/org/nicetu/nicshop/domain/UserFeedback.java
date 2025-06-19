package org.nicetu.nicshop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_feedback")
@Getter
@Setter
@NoArgsConstructor
public class UserFeedback {
    @Id
    @Column(name = "user_feedback_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "feedback")
    private Long feedback;

    @Column(name = "comment")
    private String comment;

    @OneToMany(mappedBy = "userFeedback", cascade = CascadeType.ALL)
    private List<Photo> photos = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Item item;
}
