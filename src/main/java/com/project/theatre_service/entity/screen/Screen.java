package com.project.theatre_service.entity.screen;

import com.project.theatre_service.entity.theatre.Theatre;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "screens", uniqueConstraints = {
        @UniqueConstraint(
                name = "uq_screens_theatre_number",
                columnNames = {"theatre_id","screen_number"}
        )
})
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theatre_id", nullable = false)
    private Theatre theatre;
    @Column(name = "screen_number", nullable = false)
    @Positive
    private Integer screenNumber;
    @Column(name = "name", length = 100)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "screen_type", length = 30, nullable = false)
    private ScreenType screenType;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private ScreenStatus status = ScreenStatus.ACTIVE;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}
