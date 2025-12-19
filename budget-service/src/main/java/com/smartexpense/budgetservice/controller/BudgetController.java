package com.smartexpense.budgetservice.controller;

import com.smartexpense.budgetservice.dto.BudgetRequestDTO;
import com.smartexpense.budgetservice.dto.BudgetResponseDTO;
import com.smartexpense.budgetservice.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService service;

    @PostMapping
    public BudgetResponseDTO create(@RequestBody @Valid BudgetRequestDTO dto) {
        Long userId = getUserId();
        return service.create(dto, userId);
    }

    @GetMapping("/me")
    public List<BudgetResponseDTO> myBudgets(@RequestParam String month) {
        return service.getMyBudgets(getUserId(), month);
    }

    @PutMapping("/{id}")
    public BudgetResponseDTO update(
            @PathVariable Long id,
            @RequestBody @Valid BudgetRequestDTO dto) {
        return service.update(id, dto, getUserId());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id, getUserId());
    }

    private Long getUserId() {
        return Long.parseLong(
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName()
        );
    }
}

