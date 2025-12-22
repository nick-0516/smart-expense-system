package com.smartexpense.budgetservice.service;
import com.smartexpense.budgetservice.client.UserClient;
import com.smartexpense.budgetservice.client.dto.UserResponse;
import com.smartexpense.budgetservice.dto.BudgetRequestDTO;
import com.smartexpense.budgetservice.dto.BudgetResponseDTO;
import com.smartexpense.budgetservice.exception.ResourceNotFoundException;
import com.smartexpense.budgetservice.model.Budget;
import com.smartexpense.budgetservice.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository repository;
    private final UserClient userClient;

    @Transactional
    public BudgetResponseDTO create(BudgetRequestDTO dto, String email) {

        Long userId = resolveUserId(email);

        repository.findByUserIdAndCategoryAndMonth(
                userId, dto.getCategory(), dto.getMonth()
        ).ifPresent(b -> {
            throw new IllegalStateException("Budget already exists for this category and month");
        });

        Budget budget = Budget.builder()
                .userId(userId)
                .category(dto.getCategory())
                .monthlyLimit(dto.getMonthlyLimit())
                .month(dto.getMonth())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return toDTO(repository.save(budget));
    }

    @Transactional(readOnly = true)
    public List<BudgetResponseDTO> getByMonth(String month, String email) {

        Long userId = resolveUserId(email);

        return repository.findByUserIdAndMonth(userId, month)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public BudgetResponseDTO update(Long id, BudgetRequestDTO dto, String email) {

        Budget budget = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found: " + id));

        Long userId = resolveUserId(email);

        if (!budget.getUserId().equals(userId)) {
            throw new AccessDeniedException("Not your budget, You are not allowed to update this budget");
        }

        budget.setMonthlyLimit(dto.getMonthlyLimit());
        budget.setUpdatedAt(LocalDateTime.now());

        return toDTO(repository.save(budget));
    }

    @Transactional
    public void delete(Long id, String email) {

        Budget budget = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found: " + id));

        Long userId = resolveUserId(email);

        if (!budget.getUserId().equals(userId)) {
            throw new AccessDeniedException("Not your budget, You are not allowed to update this budget");
        }

        repository.delete(budget);
    }

    //helper methods
    private Long resolveUserId(String email) {
        try {
            UserResponse user = userClient.getByEmail(email);
            if (user == null) {
                throw new ResourceNotFoundException("User not found with email: " + email);
            }
            return user.id();
        } catch (Exception ex) {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }
    }
    private BudgetResponseDTO toDTO(Budget budget) {
        return BudgetResponseDTO.builder()
                .id(budget.getId())
                .category(budget.getCategory())
                .monthlyLimit(budget.getMonthlyLimit())
                .month(budget.getMonth())
                .build();
    }
}