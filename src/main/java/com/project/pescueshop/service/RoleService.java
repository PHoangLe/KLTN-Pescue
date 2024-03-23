package com.project.pescueshop.service;

import com.project.pescueshop.model.entity.Role;
import com.project.pescueshop.repository.inteface.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService extends BaseService {
    private final RoleRepository roleRepository;

    public List<Role> getDefaultUserRole(){
        return roleRepository.getDefaultUserRole();
    }
}
