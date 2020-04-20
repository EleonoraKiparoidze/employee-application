package com.rustedbrain.study.employeeservice.model.security;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@ToString
public class UserDto {

    @EqualsAndHashCode.Include
    private String username;

    private Role role;

}
