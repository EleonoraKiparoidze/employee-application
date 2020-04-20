package com.rustedbrain.study.employeeservice.controller;

import com.rustedbrain.study.employeeservice.configuration.security.AuthTokenFilter;
import com.rustedbrain.study.employeeservice.converter.UserDtoToUserBiConverter;
import com.rustedbrain.study.employeeservice.model.security.User;
import com.rustedbrain.study.employeeservice.model.security.UserDto;
import com.rustedbrain.study.employeeservice.model.security.request.LoginRequest;
import com.rustedbrain.study.employeeservice.model.security.request.RegistrationRequest;
import com.rustedbrain.study.employeeservice.model.security.response.JwtResponse;
import com.rustedbrain.study.employeeservice.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authentication")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final UserServiceImpl userService;

    private final UserDtoToUserBiConverter userDtoToUserBiConverter;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, UserServiceImpl userService,
                                    UserDtoToUserBiConverter userDtoToUserBiConverter) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.userDtoToUserBiConverter = userDtoToUserBiConverter;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JwtResponse> authenticate(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = AuthTokenFilter.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(token, username, roles));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest request) {
        if (userService.isUsernameUsed(request.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already used!");
        }
        User user = userService.registerUser(request);
        return ResponseEntity.ok(userDtoToUserBiConverter.convert(user));
    }
}
