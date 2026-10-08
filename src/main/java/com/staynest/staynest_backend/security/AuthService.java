package com.staynest.staynest_backend.security;

import com.staynest.staynest_backend.advice.ErrorCode;
import com.staynest.staynest_backend.dto.LogInDto;
import com.staynest.staynest_backend.dto.SignUpDto;
import com.staynest.staynest_backend.dto.TokenDto;
import com.staynest.staynest_backend.dto.UserDto;
import com.staynest.staynest_backend.entity.User;
import com.staynest.staynest_backend.entity.enums.Roles;
import com.staynest.staynest_backend.exception.DuplicateResource;
import com.staynest.staynest_backend.exception.ResourceNotFound;
import com.staynest.staynest_backend.mapper.UserMapper;
import com.staynest.staynest_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public UserDto signUp(SignUpDto signUpDto) {

        // check if user already exists
        if(userRepository.findByEmail(signUpDto.email()).isPresent()) {
            throw new DuplicateResource("user", signUpDto.email(), ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = userMapper.toEntity(signUpDto);
        user.setRoles(Set.of(Roles.GUEST));
        user.setPassword(passwordEncoder.encode(signUpDto.password()));

        user = userRepository.save(user);

        return userMapper.toDto(user);

    }

    public TokenDto logIn(LogInDto logInDto) {


        // authenticate token
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                logInDto.email(),
                logInDto.password()
        );

        // authenticate user
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        User user = (User) authenticate.getPrincipal();
        if(user == null) {
            throw new ResourceNotFound("user", logInDto.email(), ErrorCode.USER_NOT_FOUND);
        }
        // authentication successful
        String access_token = jwtService.generateAccessToken(user);
        String refresh_token = jwtService.generateRefreshToken(user);
        return new TokenDto(access_token, refresh_token);
    }

    public TokenDto refreshToken(String refresh_token) {

        String email = jwtService.extractUsername(refresh_token);
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFound("user", email, ErrorCode.USER_NOT_FOUND));

        String accessToken = jwtService.generateAccessToken(user);

        return new TokenDto(accessToken,refresh_token);
    }

}
