package com.terrasi.terrasiapi.repository;

import com.terrasi.terrasiapi.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<Alert, Long> {
}
