package com.staynest.staynest_backend.services.impl;

import com.staynest.staynest_backend.entity.User;
import com.staynest.staynest_backend.exception.ResourceNotFound;
import com.staynest.staynest_backend.repository.UserRepository;
import com.staynest.staynest_backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.staynest.staynest_backend.advice.ErrorCode.EMAIL_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService , UserService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFound("user", "email", EMAIL_NOT_FOUND));

        return user;
    }

}
