package com.ml.testsexamples.dto;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Data
@Entity
@Table(name = "customer_data", indexes = {
        @Index(name = "idx_customer_data_id", columnList = "id", unique = true)
})
public final class CustomerDataDto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Exclude
    private Long id;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    private double balance;

    private double minimumBalance;

    @NonNull
    @Builder.Default
    private Date createdDate = new Date();
}