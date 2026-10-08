package com.staynest.staynest_backend.controller.guest;

import com.staynest.staynest_backend.dto.LogInDto;
import com.staynest.staynest_backend.dto.SignUpDto;
import com.staynest.staynest_backend.dto.TokenDto;
import com.staynest.staynest_backend.dto.UserDto;
import com.staynest.staynest_backend.security.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpDto signUpDto) {
        return ResponseEntity.ok(authService.signUp(signUpDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> logIn(@RequestBody LogInDto logInDto , HttpServletResponse response) {
        TokenDto tokenDto = authService.logIn(logInDto);
        Cookie cookie = new Cookie("refresh_token", tokenDto.refreshToken());
        cookie.setHttpOnly(true); // js no access
        cookie.setSecure(true);
        cookie.setMaxAge(5000);
        response.addCookie(cookie);
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenDto> refreshToken(@CookieValue("refresh_token") String refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }
}
