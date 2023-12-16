package com.cofisweak.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sessions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString(exclude = "user")
public class Session implements Serializable {
    @Id
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;
    @Column(name = "expiresAt", nullable = false)
    private LocalDateTime expiresAt;
}
