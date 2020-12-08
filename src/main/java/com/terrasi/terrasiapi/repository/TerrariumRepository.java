package com.terrasi.terrasiapi.repository;

import com.terrasi.terrasiapi.model.Terrarium;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TerrariumRepository extends JpaRepository<Terrarium, Long> {

    @Query("SELECT t FROM Terrarium t WHERE t.user.id = :userId")
    Page<Terrarium> getAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT t FROM Terrarium t WHERE t.terrariumSettings.id = :settingsId")
    Optional<Terrarium> getByTerrariumSettings(@Param("settingsId") Long terrariumSettingsId);
}
