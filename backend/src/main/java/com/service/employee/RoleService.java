package com.service.employee;

import com.model.employee.Role;
import com.repository.employee.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    public Role findRoleById(Integer roleId) {
        return roleRepository.findById(roleId).orElse(null);
    }

    public Role findRoleByName(String roleName) {
        return roleRepository.findByName(roleName);
    }

    public void saveNewRole(Role newRole) {
        roleRepository.save(newRole);
    }

    public void updateRole(Integer roleId, Role newRole) {
        Role role = findRoleById(roleId);

        newRole.setId(roleId);
        newRole.setUsers(role.getUsers());

        roleRepository.save(newRole);
    }

    public void deleteRole(Integer roleId) {
        Role role = findRoleById(roleId);

        roleRepository.delete(role);
    }

    public boolean isRoleExists(Integer roleId) {
        return roleRepository.existsById(roleId);
    }

    public Map<String, Object> checkRoleAndGetErrorsMap(Role newRole) {
        Map<String, Object> errors = new HashMap<>();

        if(roleRepository.existsByName(newRole.getName())) {
            errors.put("field(name) error", "role with name = " + newRole.getName() + " already exist");
        }

        return errors;
    }
}

