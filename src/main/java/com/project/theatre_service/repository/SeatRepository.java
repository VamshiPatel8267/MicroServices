package com.project.theatre_service.repository;

import com.project.theatre_service.entity.seat.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByScreen_Id(Long ScreenId);
}
