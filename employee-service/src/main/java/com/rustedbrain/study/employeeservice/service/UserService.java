package com.rustedbrain.study.employeeservice.service;

import com.rustedbrain.study.employeeservice.model.security.User;
import com.rustedbrain.study.employeeservice.model.security.request.RegistrationRequest;

public interface UserService {
    User registerUser(RegistrationRequest registrationRequest);
}
