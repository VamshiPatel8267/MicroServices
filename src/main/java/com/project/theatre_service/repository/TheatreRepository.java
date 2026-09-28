package com.project.theatre_service.repository;

import com.project.theatre_service.entity.theatre.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, Long> {
    List<Theatre> findByCity_Id(Long cityId);
}
