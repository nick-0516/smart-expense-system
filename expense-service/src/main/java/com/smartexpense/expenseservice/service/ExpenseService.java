package com.smartexpense.expenseservice.service;

import com.smartexpense.expenseservice.dto.ExpenseDTO;
import com.smartexpense.expenseservice.exception.ResourceNotFoundException;
import com.smartexpense.expenseservice.mapper.ExpenseMapper;
import com.smartexpense.expenseservice.model.Category;
import com.smartexpense.expenseservice.model.Expense;
import com.smartexpense.expenseservice.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository repo;

    public ExpenseDTO create(ExpenseDTO dto) {
        Expense saved = repo.save(ExpenseMapper.toEntity(dto));
        return ExpenseMapper.toDTO(saved);
    }

    public ExpenseDTO getById(Long id) {
        Expense e = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found: " + id));
        return ExpenseMapper.toDTO(e);
    }

    public List<ExpenseDTO> getByUser(Long userId) {
        return repo.findByUserId(userId).stream().map(ExpenseMapper::toDTO).toList();
    }

    public List<ExpenseDTO> getByDateRange(Long userId, LocalDate from, LocalDate to) {
        return repo.findByUserIdAndExpenseDateBetween(userId, from, to).stream().map(ExpenseMapper::toDTO).toList();
    }

    public List<ExpenseDTO> getByCategory(Long userId, Category category) {
        return repo.findByUserIdAndCategory(userId, category).stream().map(ExpenseMapper::toDTO).toList();
    }

    public ExpenseDTO update(Long id, ExpenseDTO dto) {
        Expense existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found: " + id));
        existing.setUserId(dto.getUserId());
        existing.setCategory(dto.getCategory());
        existing.setAmount(dto.getAmount());
        existing.setExpenseDate(dto.getExpenseDate());
        existing.setNote(dto.getNote());
        return ExpenseMapper.toDTO(repo.save(existing));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Expense not found: " + id);
        repo.deleteById(id);
    }
}