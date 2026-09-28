package com.project.theatre_service.repository;

import com.project.theatre_service.entity.city.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City , Long> {

}
