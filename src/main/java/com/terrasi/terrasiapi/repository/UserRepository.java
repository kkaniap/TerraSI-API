package com.terrasi.terrasiapi.repository;

import com.terrasi.terrasiapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
