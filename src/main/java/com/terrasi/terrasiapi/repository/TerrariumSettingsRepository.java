package com.terrasi.terrasiapi.repository;

import com.terrasi.terrasiapi.model.TerrariumSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TerrariumSettingsRepository extends JpaRepository<TerrariumSettings, Long> {
}
