package com.terrasi.terrasiapi.repository;

import com.terrasi.terrasiapi.model.Terrarium;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TerrariumRepository extends JpaRepository<Terrarium, Long> {

    @Query("SELECT t FROM Terrarium t WHERE t.user.id = :userId")
    Page<Terrarium> getAllByUserId(@Param("userId") Long userId, Pageable pageable);
}
