package com.terrasi.terrasiapi.repository;

import com.terrasi.terrasiapi.model.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {
}
