package com.company.firebird4.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "FB_GROUP")
@Entity(name = "firebird4_Group")
@NamePattern("%s|number")
public class Group extends StandardEntity {
    private static final long serialVersionUID = 4239849314470875578L;

    @NotNull
    @Column(name = "NUMBER_", nullable = false)
    private Integer number;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}