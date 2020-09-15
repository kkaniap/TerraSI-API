package com.terrasi.terrasiapi.repository;

import com.terrasi.terrasiapi.model.Terrarium;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TerrariumRepository extends JpaRepository<Terrarium, Long> {
}
