package com.smartexpense.expenseservice.service;

import com.smartexpense.expenseservice.client.dto.UserResponse;
import com.smartexpense.expenseservice.client.UserClient;
import com.smartexpense.expenseservice.dto.ExpenseDTO;
import com.smartexpense.expenseservice.exception.ResourceNotFoundException;
import com.smartexpense.expenseservice.mapper.ExpenseMapper;
import com.smartexpense.expenseservice.model.Category;
import com.smartexpense.expenseservice.model.Expense;
import com.smartexpense.expenseservice.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final UserClient userClient; // Feign client to call user-service
    private final ExpenseRepository repo;

    // CREATE: deriving userId from email (from JWT)
    @Transactional
    public ExpenseDTO createForEmail(ExpenseDTO dto, String email) {
        UserResponse user = fetchUserByEmailOrThrow(email);
        Expense entity = ExpenseMapper.toEntity(dto);
        entity.setUserId(user.id());
        Expense saved = repo.save(entity);
        return ExpenseMapper.toDTO(saved);
    }

    //GET all of a user by authenticated email
    @Transactional(readOnly = true)
    public List<ExpenseDTO> getByUserEmail(String email) {
        UserResponse user = fetchUserByEmailOrThrow(email);
        return repo.findByUserId(user.id()).stream().map(ExpenseMapper::toDTO).toList();
    }

    // READ by id while ensuring ownership
    @Transactional(readOnly = true)
    public ExpenseDTO getByIdForEmail(Long id, String email) {
        Expense e = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense not found: " + id));
        UserResponse user = fetchUserByEmailOrThrow(email);
        if(!e.getUserId().equals(user.id())){
            throw new AccessDeniedException("You are not allowed to access this expense");
        }
        return ExpenseMapper.toDTO(e);
    }

    // GET by date range for authenticated user
    @Transactional(readOnly = true)
    public List<ExpenseDTO> getByDateRangeForEmail(String email, LocalDate from, LocalDate to) {
        UserResponse user = fetchUserByEmailOrThrow(email);
        return repo.findByUserIdAndExpenseDateBetween(user.id(), from, to).stream().map(ExpenseMapper::toDTO).toList();
    }

    // GET by category for authenticated user
    @Transactional(readOnly = true)
    public List<ExpenseDTO> getByCategoryForEmail(String email, Category category) {
        UserResponse user = fetchUserByEmailOrThrow(email);
        return repo.findByUserIdAndCategory(user.id(), category).stream().map(ExpenseMapper::toDTO).toList();
    }

    // UPDATE: ensuring expense belongs to user, then update fields (ignore dto.userId)
    @Transactional
    public ExpenseDTO updateForEmail(Long id, ExpenseDTO dto, String email) {
        Expense existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found: " + id));
        UserResponse user = fetchUserByEmailOrThrow(email);
        if (!existing.getUserId().equals(user.id())) {
            throw new AccessDeniedException("You are not allowed to update this expense");
        }
        //existing.setUserId(dto.getUserId()); we are deriving ownership from JWT, dto.userid cannot be trusted now.
        existing.setCategory(dto.getCategory());
        existing.setAmount(dto.getAmount());
        existing.setExpenseDate(dto.getExpenseDate());
        existing.setNote(dto.getNote());
        return ExpenseMapper.toDTO(repo.save(existing));
    }

    // DELETE: ensuring ownership before deleting
    @Transactional
    public void deleteForEmail(Long id, String email) {
        Expense existing = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense not found: " + id));
        UserResponse user = fetchUserByEmailOrThrow(email);
        if (!existing.getUserId().equals(user.id())) {
            throw new AccessDeniedException("You are not allowed to delete this expense");
        }
        repo.deleteById(id);
    }

    //helper method to fetch user from user-service
    private UserResponse fetchUserByEmailOrThrow(String email) {
        try {
            UserResponse user = userClient.getByEmail(email);
            if (user == null) {
                throw new ResourceNotFoundException("User not found with email: " + email);
            }
            return user;
        } catch (Exception ex) {
            // normalizing feign errors into ResourceNotFound
            throw new ResourceNotFoundException(ex.getMessage() + email);
        }
    }
}