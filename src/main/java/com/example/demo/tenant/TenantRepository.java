package com.example.demo.tenant;

import org.springframework.data.jpa.repository.JpaRepository;

interface TenantRepository extends JpaRepository<Tenant, Integer> {

}