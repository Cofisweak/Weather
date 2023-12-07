package com.cofisweak.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users", indexes = {@Index(columnList = "login", unique = true)})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString(exclude = {"password"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String login;
    private String password;
}
