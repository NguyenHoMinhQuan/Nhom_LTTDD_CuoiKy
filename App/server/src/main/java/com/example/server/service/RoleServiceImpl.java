package com.example.server.service;

import com.example.server.dto.RoleDTO;
import com.example.server.entity.Role;
import com.example.server.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService{
    
    @Autowired
    private RoleRepository roleRepository; // Inject Repository vào Service

    // ********** Các phương thức Mapping Helper **********
    private RoleDTO convertToDTO(Role role) {
        return new RoleDTO(role.getRoleId(), role.getRoleName());
    }

    private Role convertToEntity(RoleDTO roleDTO) {
        // Ánh xạ từ DTO sang Entity
        Role role = new Role();
        role.setRoleId(roleDTO.getRoleId());
        role.setRoleName(roleDTO.getRoleName());
        return role;
    }

    // ********** Triển khai Service **********
    
    @Override
    public List<RoleDTO> findAllRoles() {
        // 1. Dùng Repository lấy Entity List
        List<Role> roles = roleRepository.findAll();
        // 2. Chuyển List Entity sang List DTO
        return roles.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
    }

    @Override
    public Optional<RoleDTO> findRoleById(Integer id) {
        // 1. Dùng Repository tìm Entity theo ID
        return roleRepository.findById(id)
            // 2. Chuyển Optional<Entity> sang Optional<DTO>
            .map(this::convertToDTO);
    }
    
    @Override
    public RoleDTO saveRole(RoleDTO roleDTO) {
        // 1. Chuyển DTO sang Entity
        Role roleToSave = convertToEntity(roleDTO);
        // 2. Lưu Entity (Spring Data JPA tự động INSERT nếu Id null/0 hoặc UPDATE nếu Id đã tồn tại)
        Role savedRole = roleRepository.save(roleToSave);
        // 3. Trả về DTO của Entity đã được lưu
        return convertToDTO(savedRole);
    }

    @Override
    public void deleteRole(Integer id) {
        roleRepository.deleteById(id);
    }

}
