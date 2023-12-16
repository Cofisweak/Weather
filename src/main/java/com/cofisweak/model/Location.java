package com.cofisweak.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "locations",
        indexes = @Index(columnList = "name, state, userid", unique = true))
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString(exclude = "user")
public class Location implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String name;
    private String state;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false)
    private User user;
    @Column(nullable = false)
    private BigDecimal latitude;
    @Column(nullable = false)
    private BigDecimal longitude;
}
