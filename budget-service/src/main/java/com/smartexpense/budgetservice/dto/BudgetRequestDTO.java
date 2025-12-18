package com.smartexpense.budgetservice.dto;

import com.smartexpense.budgetservice.model.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BudgetRequestDTO {
    @NotNull
    private Category category;

    @NotNull
    @Positive
    private BigDecimal monthlyLimit;

    @NotBlank
    private String month;
}
