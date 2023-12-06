package com.cofisweak.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users", indexes = {@Index(columnList = "login", unique = true)})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString(exclude = "locations")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String login;
    private String password;
    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<Location> locations = new ArrayList<>();
}
