package com.example.demo.tenant;

import com.example.demo.core.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @PostMapping
    public Tenant create(@RequestBody Tenant tenant) {
        return tenantService.create(tenant);
    }

    @GetMapping
    public List<Tenant> findAll() {
        return tenantService.findAll();
    }

    @PatchMapping("/{id}")
    public Tenant updateById(@PathVariable("id") int id, @RequestBody Tenant tenant) {
        return tenantService.updateById(id, tenant);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") int id) {
        tenantService.deleteById(id);
    }

    @GetMapping("/impersonate/{tenantId}")
    public ResponseEntity impersonate(@PathVariable("tenantId") int tenantId) {
        String jwt = tenantService.impersonate(tenantId);
        return ResponseEntity.ok()
                .header(Constants.AUTHORIZATION_HEADER, Constants.BEARER_SCHEMA + jwt)
                .body(null);
    }
}