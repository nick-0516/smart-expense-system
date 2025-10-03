package com.smartexpense.userservice.service;

import com.smartexpense.userservice.dto.UserDTO;
import com.smartexpense.userservice.model.User;
import com.smartexpense.userservice.repository.UserRepository;
import com.smartexpense.userservice.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // this generates constructor for all 'final' fields
public class UserService {
    private final UserRepository userRepository; // lombok gnerates a constructor for this at compile time i.e., Constructor injection
    private final org.springframework.security.crypto.password.PasswordEncoder encoder;

    public UserDTO createUser(UserDTO dto) {
        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .build();
        User saved = userRepository.save(user);
        //dto.setId(saved.getId());
        return UserDTO.builder()
                .id(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .password(null) //omit password ideally
                .build();
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

    public UserDTO getUserById(long id){
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        return UserDTO.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .build();
    }

    public UserDTO updateUser(long id, UserDTO updatedUser){
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());
        User newUser = userRepository.save(user);
        // newUser.setId(user.getId()); no need for this
        return UserDTO.builder()
                        .id(newUser.getId())
                        .name(newUser.getName())
                        .email(newUser.getEmail())
                        .password(newUser.getPassword())
                        .build();
    }
}