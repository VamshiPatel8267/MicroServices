package com.project.theatre_service.entity.theatre;

import com.project.theatre_service.entity.city.City;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "theatres")
public class Theatre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false, length = 200)
    private String name;
    @Column(name = "address", nullable = false, length = 500)
    private String address;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TheatreStatus status = TheatreStatus.ACTIVE;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
