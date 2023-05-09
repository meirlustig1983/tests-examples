package com.ml.testsexamples.dao;

import lombok.*;
import jakarta.persistence.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;

@Builder
@Accessors(chain = true)
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Data
@Entity
@Table(name = "bank_account")
public class BankAccount {

    @Id
    @SequenceGenerator(
            name = "bank_account_id_sequence",
            sequenceName = "bank_account_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "bank_account_id_sequence"
    )
    @EqualsAndHashCode.Exclude
    private Long id;

    @NonNull
    @Column(nullable = false)
    private String firstName;

    @NonNull
    @Column(nullable = false)
    private String lastName;

    private BigDecimal balance;

    private BigDecimal minimumBalance;

    private boolean active;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now(Clock.systemDefaultZone());

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now(Clock.systemDefaultZone());
}