package org.nicetu.nicshop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "photo")
@Getter
@Setter
@NoArgsConstructor
public class Photo {
    @Id
    @Column(name = "photo_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "picture_url")
    private String pictureUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_feedback_id")
    private UserFeedback userFeedback;
}