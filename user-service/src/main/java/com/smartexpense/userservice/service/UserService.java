package com.smartexpense.userservice.service;

import com.smartexpense.userservice.dto.UserDTO;
import com.smartexpense.userservice.dto.UserResponse;
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

    public UserResponse createUser(UserDTO dto) {
        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .build();
        User saved = userRepository.save(user);
        //dto.setId(saved.getId());
        return new UserResponse(saved.getId(), saved.getName(), saved.getEmail());
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail()
                ))
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(long id){
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }

    public UserResponse getUserByEmail(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with Email: " + email));
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }

    public UserResponse updateUser(long id, UserDTO updatedUser){
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(encoder.encode(updatedUser.getPassword()));
        User newUser = userRepository.save(user);
        // newUser.setId(user.getId()); no need for this
        return new UserResponse(newUser.getId(), newUser.getName(), newUser.getEmail());
    }
}