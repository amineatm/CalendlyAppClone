package miu.edu.mpp.app.controller;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import miu.edu.mpp.app.dto.user.UserLoginRequest;
import miu.edu.mpp.app.dto.user.UserLoginResponse;
import miu.edu.mpp.app.dto.user.UserLoginWrapper;
import miu.edu.mpp.app.dto.user.UserRegisterWrapper;
import miu.edu.mpp.app.security.JwtUtil;
import miu.edu.mpp.app.service.UserService;
import static org.springframework.http.HttpStatus.CREATED;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRest {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@Valid @RequestBody UserLoginWrapper request) {
        return ResponseEntity.status(201).body(userService.login(request.getUser()));
    }

    @PostMapping
    public ResponseEntity<UserLoginResponse> register(@Valid @RequestBody UserRegisterWrapper body) {
        return ResponseEntity.status(CREATED).body(userService.register(body.getUser()));
    }

    @GetMapping("/user")
    public ResponseEntity<UserRo> findMe(@AuthenticationPrincipal String email) throws NotFoundException {
        // email is extracted from the JWT by a Spring‑Security AuthenticationProvider
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @PutMapping("/user")
    public ResponseEntity<UserRo> updateUser(@AuthenticationPrincipal String email,
                                             @RequestBody UpdateUserRequest request) throws NotFoundException {
        return ResponseEntity.ok(userService.update(email, request));
    }

    @DeleteMapping("/users/{slug}")
    public ResponseEntity<Void> deleteUser(@PathVariable String slug) {
        userService.delete(slug);
        return ResponseEntity.noContent().build(); // returns 204 No Content
    }


}