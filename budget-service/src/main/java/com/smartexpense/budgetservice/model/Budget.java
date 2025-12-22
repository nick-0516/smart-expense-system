package com.smartexpense.budgetservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"userId", "category", "month"}
        )
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private Category category;

    private BigDecimal monthlyLimit;

    @Column(name = "budget_month")
    private String month; // yyyy-MM

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
