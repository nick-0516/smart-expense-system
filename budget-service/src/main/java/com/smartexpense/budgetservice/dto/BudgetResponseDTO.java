package com.smartexpense.budgetservice.dto;

import com.smartexpense.budgetservice.model.Category;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BudgetResponseDTO {
    private long id;
    private Category category;
    private BigDecimal monthlyLimit;
    private String month;
}
