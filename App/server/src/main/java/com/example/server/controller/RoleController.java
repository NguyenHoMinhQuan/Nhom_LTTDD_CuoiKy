package com.example.server.controller;

import com.example.server.dto.RoleDTO;
import com.example.server.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    @Autowired
    private RoleService roleService; // 3. Inject Service vào Controller

    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roles = roleService.findAllRoles();
        return ResponseEntity.ok(roles); // Trả về HTTP 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Integer id) {
        return roleService.findRoleById(id)
            .map(ResponseEntity::ok) // Nếu tìm thấy -> Trả về HTTP 200 OK
            .orElseGet(() -> ResponseEntity.notFound().build()); // Nếu không tìm thấy -> Trả về HTTP 404 Not Found
    }

    @PostMapping
    public ResponseEntity<RoleDTO> createOrUpdateRole(@RequestBody RoleDTO roleDTO) {
        RoleDTO savedRole = roleService.saveRole(roleDTO);
        
        if (roleDTO.getRoleId() == null) {
            return new ResponseEntity<>(savedRole, HttpStatus.CREATED);
        }
        
        return ResponseEntity.ok(savedRole);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Integer id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build(); 
    }
}
