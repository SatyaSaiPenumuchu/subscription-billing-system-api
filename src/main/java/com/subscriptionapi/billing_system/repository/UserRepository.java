package com.subscriptionapi.billing_system.repository;

import com.subscriptionapi.billing_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> findByIdEager(@Param("id") Long id);



}
