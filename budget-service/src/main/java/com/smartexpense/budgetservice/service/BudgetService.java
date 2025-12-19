package com.smartexpense.budgetservice.service;

import com.smartexpense.budgetservice.dto.BudgetRequestDTO;
import com.smartexpense.budgetservice.dto.BudgetResponseDTO;
import com.smartexpense.budgetservice.model.Budget;
import com.smartexpense.budgetservice.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository repo;

    public BudgetResponseDTO create(BudgetRequestDTO dto, Long userId) {

        repo.findByUserIdAndCategoryAndMonth(
                userId, dto.getCategory(), dto.getMonth()
        ).ifPresent(b -> {
            throw new RuntimeException("Budget already exists");
        });

        Budget budget = Budget.builder()
                .userId(userId)
                .category(dto.getCategory())
                .monthlyLimit(dto.getMonthlyLimit())
                .month(dto.getMonth())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return map(repo.save(budget));
    }

    public List<BudgetResponseDTO> getMyBudgets(Long userId, String month) {
        return repo.findByUserIdAndMonth(userId, month)
                .stream()
                .map(this::map)
                .toList();
    }

    public BudgetResponseDTO update(Long id, BudgetRequestDTO dto, Long userId) {
        Budget budget = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        if (!budget.getUserId().equals(userId))
            throw new AccessDeniedException("Not your budget");

        budget.setMonthlyLimit(dto.getMonthlyLimit());
        budget.setUpdatedAt(LocalDateTime.now());

        return map(repo.save(budget));
    }

    public void delete(Long id, Long userId) {
        Budget budget = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        if (!budget.getUserId().equals(userId))
            throw new AccessDeniedException("Not your budget");

        repo.delete(budget);
    }

    private BudgetResponseDTO map(Budget b) {
        return BudgetResponseDTO.builder()
                .id(b.getId())
                .category(b.getCategory())
                .monthlyLimit(b.getMonthlyLimit())
                .month(b.getMonth())
                .build();
    }
}
