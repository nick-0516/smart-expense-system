package com.smartexpense.expenseservice.controller;

import com.smartexpense.expenseservice.dto.ExpenseDTO;
import com.smartexpense.expenseservice.model.Category;
import com.smartexpense.expenseservice.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService service;

    @PostMapping
    public ResponseEntity<ExpenseDTO> create(@RequestBody @Valid ExpenseDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        var created = service.createForEmail(dto, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @GetMapping("/myExpenses")
    public List<ExpenseDTO> getMyExpenses() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return service.getByUserEmail(email);
    }

    @GetMapping("/{id}")
    public ExpenseDTO getById(@PathVariable Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return service.getByIdForEmail(id, email);
    }

//    @GetMapping
//    public List<ExpenseDTO> getByUser(@RequestParam Long userId) {
//        return service.getByUser(userId);
//    }

    @GetMapping("/by-date")
    public List<ExpenseDTO> byDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return service.getByDateRangeForEmail(email, from, to);
    }

    @GetMapping("/by-category")
    public List<ExpenseDTO> byCategory(@RequestParam Category category) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return service.getByCategoryForEmail(email, category);
    }

    @PutMapping("/{id}")
    public ExpenseDTO update(@PathVariable Long id, @RequestBody @Valid ExpenseDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return service.updateForEmail(id, dto, email);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        service.deleteForEmail(id, email);
    }
}