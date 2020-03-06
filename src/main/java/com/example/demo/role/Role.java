package com.example.demo.role;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "roles")
public class Role {

    @Id
    private int id;

    @Column(nullable = false)
    private String name;
}