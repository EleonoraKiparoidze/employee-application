package com.rustedbrain.study.employeeservice.model.security.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegistrationRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
    private String role;
    @NotBlank
    private String password;
}
