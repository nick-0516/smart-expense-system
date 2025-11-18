package com.smartexpense.expenseservice.repository;

import com.smartexpense.expenseservice.model.Category;
import com.smartexpense.expenseservice.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserId(Long userId);
    List<Expense> findByUserIdAndExpenseDateBetween(Long userId, LocalDate from, LocalDate to);
    List<Expense> findByUserIdAndCategory(Long userId, Category category);
}