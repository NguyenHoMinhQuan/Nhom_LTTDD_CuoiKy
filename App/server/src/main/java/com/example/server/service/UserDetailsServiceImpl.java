package com.example.server.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.server.dto.UserDTO;
import com.example.server.entity.User;
import com.example.server.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private String mapRoleIdToRoleName(Byte roleId) {
        if (roleId == 4)
            return "ROLE_ADMIN";
        if (roleId == 3)
            return "ROLE_LECTURER";
        if (roleId == 2)
            return "ROLE_STUDENT";
        return "ROLE_GUEST";
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng: " + username));

        String roleName = mapRoleIdToRoleName(user.getRoleId());

        // Dùng password trực tiếp từ PasswordHash, không băm
        return new org.springframework.security.core.userdetails.User( // 2. Chuyển đổi User (Entity) -> UserDetails (Security Object)
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(roleName)));
    }

    @Override //gọi userRepository vsof db tìm data
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDTO findUserDTOByUsername(String username) {
        User user = findByUsername(username).orElse(null);
        if (user == null)
            return null;

        return new UserDTO(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                mapRoleIdToRoleName(user.getRoleId()));
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<UserDTO> findAllUserDTOs() {
        return userRepository.findAll().stream()
                .map(user -> new UserDTO(
                        user.getUserId(),
                        user.getUsername(),
                        user.getEmail(),
                        mapRoleIdToRoleName(user.getRoleId())))
                .collect(Collectors.toList());
    }
}
