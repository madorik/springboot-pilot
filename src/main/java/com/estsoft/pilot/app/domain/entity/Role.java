package com.estsoft.pilot.app.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Create by madorik on 2020-09-21
 */
@Getter
@AllArgsConstructor
public enum  Role {
    ADMIN("ROLE_ADMIN"), USER("ROLE_USER");

    private String value;

}
