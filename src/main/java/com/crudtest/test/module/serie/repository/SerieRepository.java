package com.crudtest.test.module.serie.repository;

import com.crudtest.test.module.serie.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SerieRepository extends JpaRepository<Serie, Long> {

}
