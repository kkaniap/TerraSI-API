package com.terrasi.terrasiapi.repository;

import com.terrasi.terrasiapi.model.News;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface NewsRepository extends PagingAndSortingRepository<News, Long> {
}
