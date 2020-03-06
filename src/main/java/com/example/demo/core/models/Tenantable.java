package com.example.demo.core.models;

import com.example.demo.core.listeners.TenantEntityListener;
import com.example.demo.core.utils.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
@FilterDef(name = Constants.TENANT_FILTER_NAME,
        parameters = @ParamDef(name = Constants.TENANT_PARAMETER_NAME, type = Constants.TENANT_PARAMETER_TYPE),
        defaultCondition = Constants.TENANT_COLUMN_NAME + " = :" + Constants.TENANT_PARAMETER_NAME)
@Filter(name = Constants.TENANT_FILTER_NAME)
@EntityListeners(TenantEntityListener.class)
public class Tenantable extends Auditable {

    @JsonIgnore
    @Column(nullable = false)
    private int tenantId;
}