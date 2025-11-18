package com.smartexpense.expenseservice.dto;

import com.smartexpense.expenseservice.model.Category;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDTO {
    private Long id;

    @NotNull
    private Long userId; // Part A only; Part B will ignore this and use JWT

    @NotNull
    private Category category;

    @NotNull @Positive
    private BigDecimal amount;

    @NotNull
    private LocalDate expenseDate;

    @Size(max = 255)
    private String note;
}