package com.smartexpense.budgetservice.controller;

import com.smartexpense.budgetservice.dto.BudgetRequestDTO;
import com.smartexpense.budgetservice.dto.BudgetResponseDTO;
import com.smartexpense.budgetservice.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<BudgetResponseDTO> create(
            @RequestBody @Valid BudgetRequestDTO dto) {

        String email = getEmail();
        BudgetResponseDTO created = budgetService.create(dto, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/me")
    public List<BudgetResponseDTO> getMyBudgets(
            @RequestParam String month) {

        return budgetService.getByMonth(month, getEmail());
    }

    @PutMapping("/{id}")
    public BudgetResponseDTO update(
            @PathVariable Long id,
            @RequestBody @Valid BudgetRequestDTO dto) {

        return budgetService.update(id, dto, getEmail());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        budgetService.delete(id, getEmail());
    }

    private String getEmail() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }
}