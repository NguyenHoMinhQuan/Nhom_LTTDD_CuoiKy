package com.example.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.server.service.UserDetailsServiceImpl;

import com.example.server.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService, JwtAuthFilter jwtAuthFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    // DaoAuthenticationProvider
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new org.springframework.security.authentication.ProviderManager(authenticationProvider());
    }
    @Bean
<<<<<<< HEAD
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable()) // Tắt CSRF cho API
                    // QUAN TRỌNG: Chuyển sang chế độ STATELESS (Không lưu Session trên Server)
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/api/auth/**").permitAll() // Cho phép Login/Register tự do
                            .requestMatchers("/api/users/**").permitAll()
                            .requestMatchers("/api/announcements/**").permitAll()
                            .requestMatchers("/api/classes/**").permitAll()
                            .requestMatchers("/api/students/**").permitAll()
                            .requestMatchers("/api/lecturers/**").permitAll()
                            .requestMatchers("/api/class-schedules/**").permitAll()
                            .requestMatchers("/api/registrations/**").permitAll()
                            .requestMatchers("/api/roles/**").permitAll()
                            .requestMatchers("/api/courses/**").permitAll()
                            .requestMatchers("/api/notifications/**").permitAll()
                            .requestMatchers("/api/admin/**").permitAll()
                            .anyRequest().authenticated() // Mọi request khác đều phải có Token hợp lệ
                    )
                    .authenticationProvider(authenticationProvider());

            // THÊM BỘ LỌC JWT TRƯỚC KHI XỬ LÝ ĐĂNG NHẬP MẶC ĐỊNH
            http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
        }
}
=======
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Vô hiệu hóa CSRF (bắt buộc để gọi POST/PUT/DELETE)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").permitAll() // Cho phép tất cả API bắt đầu bằng /api/
                        .anyRequest().permitAll() // Hoặc cho phép TẤT CẢ mọi request
                );
        return http.build();
    }
}
>>>>>>> ca9653ac4f017899194c71e9821fcb24fe323475
