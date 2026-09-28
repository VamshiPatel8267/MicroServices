package com.project.theatre_service.entity.seat;

import com.project.theatre_service.entity.screen.Screen;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "seats", uniqueConstraints = {
        @UniqueConstraint(name = "uq_seats_screen_row_number",
        columnNames = {"screen_id", "row_label", "seat_number"})
})
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;
    @Column(name = "row_label", nullable = false, length = 10)
    private String rowLabel;
    @Positive
    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;
    @Enumerated(EnumType.STRING)
    @Column(name = "seat_type", nullable = false, length = 30)
    private SeatType seatType;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private SeatStatus status = SeatStatus.ACTIVE;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


}
