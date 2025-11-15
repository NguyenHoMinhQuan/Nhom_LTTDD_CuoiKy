package com.example.server.service;

import com.example.server.dto.RoleDTO;
import java.util.List;
import java.util.Optional;

public interface RoleService {
    
    // Phương thức đọc: Lấy tất cả Role
    List<RoleDTO> findAllRoles();

    // Phương thức đọc: Lấy Role theo ID
    Optional<RoleDTO> findRoleById(Integer id);

    // Phương thức ghi: Tạo mới hoặc cập nhật Role
    RoleDTO saveRole(RoleDTO roleDTO);
    
    // Phương thức xóa
    void deleteRole(Integer id);

}
