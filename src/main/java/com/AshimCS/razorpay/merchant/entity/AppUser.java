package com.AshimCS.razorpay.merchant.entity;

import com.AshimCS.razorpay.common.enums.UserRole;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "app_user")
public class AppUser {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    private Merchant merchant; // many app users with diff role belong to one Marchant

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash; // encrypt the PW first using BCryptPasswordEncoder then store it in the DB. Never store plain text PW in DB

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(
//                new SimpleGrantedAuthority("ROLE_"+role)
//        );
//    }
//
//    @Override
//    public @Nullable String getPassword() {
//        return passwordHash;
//    }
//
//    @Override
//    public String getUsername() {
//        return email;
//    }
}
