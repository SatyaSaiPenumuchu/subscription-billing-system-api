package com.subscriptionapi.billing_system.service;

import com.subscriptionapi.billing_system.dto.UserRegistrationDTO;
import com.subscriptionapi.billing_system.model.*;
import com.subscriptionapi.billing_system.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SubscriptionPlanRepository planRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(UserRegistrationDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use.");
        }

        // Default plan: Free
        SubscriptionPlan plan = planRepository.findByPlanName("FREE")
                .orElseThrow(() -> new RuntimeException("Default plan not found."));

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.DEVELOPER)
                .active(true)
                .build();
        userRepository.save(user);

        // Generate API Key
        ApiKey apiKey = ApiKey.builder()
                .key(UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", ""))
                .active(true)
                .createdAt(LocalDateTime.now())
                .user(user)
                .subscriptionPlan(plan)
                .build();
        apiKeyRepository.save(apiKey);
    }

    @Transactional(readOnly = true)
    public User getUserByIdEager(Long id) {
        User user = userRepository.findByIdEager(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Force initialization of role (in case of lazy loading)
        user.getRole();

        return user;
    }
    public SubscriptionPlan getUserSubscriptionPlan(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // You may want to fetch eagerly or join fetch the apiKey and plan
        ApiKey apiKey = apiKeyRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("API key not found"));
        return apiKey.getSubscriptionPlan();
    }

    @Transactional
    public void changeUserSubscriptionPlan(Long userId, String planName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        SubscriptionPlan newPlan = planRepository.findByPlanName(planName)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        ApiKey apiKey = apiKeyRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("API key not found"));
        apiKey.setSubscriptionPlan(newPlan);
        apiKeyRepository.save(apiKey);
    }
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User createUser(User user) {
        // Add validation and password encoding as needed
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        User existingUser = getUserById(id);
        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setRole(updatedUser.getRole());
        // Update other fields as necessary
        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


}
