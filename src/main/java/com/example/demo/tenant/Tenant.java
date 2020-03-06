package com.example.demo.tenant;

import com.example.demo.core.models.Auditable;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "tenants")
public class Tenant extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;
}