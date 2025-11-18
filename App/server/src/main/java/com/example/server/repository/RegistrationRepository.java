package com.example.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.server.entity.Registration;

public interface RegistrationRepository extends JpaRepository<Registration, Integer> {

}
