package com.example.demo.user;

import com.example.demo.core.annotations.DisableTenantFilter;
import com.example.demo.core.repositories.TenantableRepository;

import java.util.Optional;

public interface UserRepository extends TenantableRepository<User> {

    @DisableTenantFilter
    Optional<User> findByEmail(String email);
}