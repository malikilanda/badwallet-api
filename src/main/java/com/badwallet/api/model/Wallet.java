package com.badwallet.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "wallets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String phoneNumber;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private Double balance;
    @Column(unique = true, nullable = false)
    private String code;
    @Column(nullable = false)
    private String currency;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}