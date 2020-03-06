package com.example.demo.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface TenantableRepository<T> extends JpaRepository<T, Integer> {

    Optional<T> findOneById(int id);
}