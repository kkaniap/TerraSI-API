package com.terrasi.terrasiapi.repository;

import com.terrasi.terrasiapi.model.AlertType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertTypeRepository  extends JpaRepository<AlertType, Long> {
}
