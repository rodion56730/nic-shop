package org.nicetu.nicshop.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User  {

    private static final String SEQUENCE_NAME = "user_sequence";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    @ElementCollection(targetClass = Role.class)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();
    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private Bucket bucket;
    private String address;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserFeedback> userFeedbacks = new ArrayList<>();


}

