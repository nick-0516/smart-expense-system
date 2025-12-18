package com.smartexpense.budgetservice.repository;

import com.smartexpense.budgetservice.model.Budget;
import com.smartexpense.budgetservice.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByUserIdAndCategoryAndMonth(Long userId, Category category, String month);
    List<Budget> findByUserIdAndMonth(Long userId, String month);
}