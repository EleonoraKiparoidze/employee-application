package com.rustedbrain.study.employeeservice.converter;

import com.rustedbrain.study.employeeservice.model.security.User;
import com.rustedbrain.study.employeeservice.model.security.UserDto;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUserBiConverter implements BiConverter<User, UserDto> {

    @NonNull
    @Override
    public UserDto convert(User user) {
        return UserDto.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    @Override
    public User convertFrom(UserDto userDto) {
        return  User.builder()
                .username(userDto.getUsername())
                .role(userDto.getRole())
                .build();
    }
}
