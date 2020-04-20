package com.rustedbrain.study.employeeservice.model.security;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User {

    private Long id;

    @EqualsAndHashCode.Include
    private String username;

    private Role role;

    private String password;
}
