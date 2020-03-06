package com.example.demo.core.listeners;

import com.example.demo.core.contexts.TenantContext;
import com.example.demo.core.models.Tenantable;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class TenantEntityListener {

    @Autowired
    private TenantContext tenantContext;

    @PrePersist
    @PreUpdate
    public void prePersistAndUpdate(Object object) {
        if (object instanceof Tenantable) {
            ((Tenantable) object).setTenantId(tenantContext.getTenantId());
        }
    }

    @PreRemove
    public void preRemove(Object object) {
        if (object instanceof Tenantable && ((Tenantable) object).getTenantId() != tenantContext.getTenantId()) {
            throw new EntityNotFoundException();
        }
    }
}