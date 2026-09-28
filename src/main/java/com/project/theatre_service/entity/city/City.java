package com.project.theatre_service.entity.city;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name" , nullable = false, length = 100)
    private  String name;
    @Column(name = "state" , nullable = false, length = 100)
    private String state;
    @Column(name = "country", nullable = false , length = 100)
    private String country;
    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private CityStatus status = CityStatus.ACTIVE;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}
