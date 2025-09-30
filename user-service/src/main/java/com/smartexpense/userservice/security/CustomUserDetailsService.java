package com.smartexpense.userservice.security;

import com.smartexpense.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import static org.springframework.security.core.userdetails.User.withUsername;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        var user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found: "+email));
        return withUsername(user.getEmail()).password(user.getPassword()).roles("USER").build();
    }
}
