package com.project.theatre_service.repository;

import com.project.theatre_service.entity.screen.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Long> {
    List<Screen> findByTheatreId(Long theatreId);
}
