package com.project.pescueshop.repository.jpa;

import com.project.pescueshop.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    @Query("SELECT r FROM Role r " +
            "WHERE r.roleName = 'ROLE_CUSTOMER' ")
    public List<Role> getDefaultUserRole();
}
