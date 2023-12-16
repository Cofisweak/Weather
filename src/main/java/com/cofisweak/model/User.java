package com.cofisweak.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users", indexes = {@Index(columnList = "login", unique = true)})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString(exclude = {"password"})
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String login;
    @Column(nullable = false)
    private String password;
}
