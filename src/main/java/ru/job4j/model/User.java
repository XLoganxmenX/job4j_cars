package ru.job4j.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "auto_user")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    private String name;
    private String login;
    private String password;
}
