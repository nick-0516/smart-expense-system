package com.smartexpense.userservice.service;

import com.smartexpense.userservice.dto.UserDTO;
import com.smartexpense.userservice.model.User;
import com.smartexpense.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // this generates constructor for all 'final' fields
public class UserService {
    private final UserRepository userRepository; // lombok gnerates a constructor for this at compile time i.e., Constructor injection

    public UserDTO createUser(UserDTO dto) {
        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
        User saved = userRepository.save(user);
        dto.setId(saved.getId());
        return dto;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> UserDTO.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .build())
                .collect(Collectors.toList());
    }
}