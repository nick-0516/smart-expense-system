package com.smartexpense.expenseservice.controller;

import com.smartexpense.expenseservice.dto.ExpenseDTO;
import com.smartexpense.expenseservice.model.Category;
import com.smartexpense.expenseservice.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
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
        var created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ExpenseDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<ExpenseDTO> getByUser(@RequestParam Long userId) {
        return service.getByUser(userId);
    }

    @GetMapping("/by-date")
    public List<ExpenseDTO> byDate(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return service.getByDateRange(userId, from, to);
    }

    @GetMapping("/by-category")
    public List<ExpenseDTO> byCategory(@RequestParam Long userId, @RequestParam Category category) {
        return service.getByCategory(userId, category);
    }

    @PutMapping("/{id}")
    public ExpenseDTO update(@PathVariable Long id, @RequestBody @Valid ExpenseDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}