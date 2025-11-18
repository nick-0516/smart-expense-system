package com.smartexpense.expenseservice.mapper;

import com.smartexpense.expenseservice.dto.ExpenseDTO;
import com.smartexpense.expenseservice.model.Expense;

public final class ExpenseMapper {
    private ExpenseMapper() {}

    public static Expense toEntity(ExpenseDTO dto) {
        if (dto == null) return null;
        return Expense.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .category(dto.getCategory())
                .amount(dto.getAmount())
                .expenseDate(dto.getExpenseDate())
                .note(dto.getNote())
                .build();
    }

    public static ExpenseDTO toDTO(Expense e) {
        if (e == null) return null;
        return ExpenseDTO.builder()
                .id(e.getId())
                .userId(e.getUserId())
                .category(e.getCategory())
                .amount(e.getAmount())
                .expenseDate(e.getExpenseDate())
                .note(e.getNote())
                .build();
    }
}