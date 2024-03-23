package com.project.pescueshop.repository.inteface;

import com.project.pescueshop.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT u FROM User u WHERE u.userEmail = ?1")
    Optional<User> findByEmail(String email);
}
