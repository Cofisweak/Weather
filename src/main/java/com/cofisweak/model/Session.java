package com.cofisweak.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sessions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Session {
    @Id
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    @Column(name = "expiresAt")
    private LocalDateTime expiresAt;
}
