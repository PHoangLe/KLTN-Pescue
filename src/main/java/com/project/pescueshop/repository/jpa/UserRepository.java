package com.project.pescueshop.repository.jpa;

import com.project.pescueshop.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT u FROM User u WHERE u.userEmail = ?1")
    Optional<User> findByEmail(String email);

    @Query(value = "INSERT INTO users_roles (user_id, role_id) VALUES (?1, ?2)", nativeQuery = true)
    void addUserRole(String userId, String roleId);

    @Query(value = "DELETE FROM users_roles WHERE user_id = ?1 AND role_id = ?2", nativeQuery = true)
    void removeUserRole(String userId, String roleId);
}
