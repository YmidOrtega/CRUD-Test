package com.crudtest.test.module.serie.repository;

import com.crudtest.test.module.serie.model.Episode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EpisodeRepository extends JpaRepository <Episode, Long> {

}
