package com.example.demo.user;

import com.example.demo.core.models.Tenantable;
import com.example.demo.role.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "users")
public class User extends Tenantable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ManyToOne(optional = false)
    private Role role;

    @Column(nullable = false)
    private String name;
}