package com.rustedbrain.study.employeeservice.service;

import com.rustedbrain.study.employeeservice.model.security.Role;
import com.rustedbrain.study.employeeservice.model.security.User;
import com.rustedbrain.study.employeeservice.model.security.request.RegistrationRequest;
import com.rustedbrain.study.employeeservice.repository.UserInMemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserInMemoryRepository inMemoryRepository;

    private final PasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserInMemoryRepository inMemoryRepository, PasswordEncoder encoder) {
        this.inMemoryRepository = inMemoryRepository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameToFind) throws UsernameNotFoundException {
        User storedUser = inMemoryRepository.findByUsername(usernameToFind)
                .orElseThrow(() -> new UsernameNotFoundException("User with the username \"" + usernameToFind + "\" not found"));

        String username = storedUser.getUsername();
        String password = storedUser.getPassword();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(storedUser.getRole().name());

        return org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(password)
                .authorities(authority)
                .build();
    }

    @PostConstruct
    public void init() {
        User user = new User();
        user.setUsername("admin");
        user.setRole(Role.ADMIN);

        String encodedAdminPassword = encoder.encode("admin");
        user.setPassword(encodedAdminPassword);

        this.inMemoryRepository.save(user);
    }

    @Override
    public User registerUser(RegistrationRequest request) {
        Role role = Optional.ofNullable(request.getRole()).map(Role::valueOf).orElse(Role.USER);

        User user = User.builder().username(request.getUsername())
                .role(role)
                .password(encoder.encode(request.getPassword()))
                .build();

        return inMemoryRepository.save(user);
    }

    public boolean isUsernameUsed(String username) {
        return inMemoryRepository.isUsernameUsed(username);
    }
}
