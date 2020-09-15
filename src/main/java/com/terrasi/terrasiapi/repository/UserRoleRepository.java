package com.terrasi.terrasiapi.repository;

import com.terrasi.terrasiapi.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
