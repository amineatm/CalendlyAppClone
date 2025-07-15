package miu.edu.mpp.app.controller;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import miu.edu.mpp.app.dto.user.*;
import miu.edu.mpp.app.security.CurrentUser;
import miu.edu.mpp.app.security.JwtUtil;
import miu.edu.mpp.app.security.UserContext;
import miu.edu.mpp.app.service.UserService;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

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
        return ResponseEntity.status(OK).body(userService.login(request.getUser()));
    }

    @PostMapping
    public ResponseEntity<UserLoginResponse> register(@Valid @RequestBody UserRegisterWrapper body) {
        return ResponseEntity.status(CREATED).body(userService.register(body.getUser()));
    }

    @GetMapping()
    public ResponseEntity<UserRo> findMe() throws NotFoundException {
        // email is extracted from the JWT by a Spring‑Security AuthenticationProvider
        CurrentUser user = UserContext.get();
        return ResponseEntity.ok(userService.findByEmail(user.getEmail()));
    }

    @PutMapping()
    public ResponseEntity<UserRo> updateUser(
                                             @RequestBody UpdateUserRequest request) throws NotFoundException {
        CurrentUser user = UserContext.get();
        return ResponseEntity.ok(userService.update(user.getEmail(), request));
    }

    @DeleteMapping("/users/{slug}")
    public ResponseEntity<Void> deleteUser(@PathVariable String slug) {
        userService.delete(slug);
        return ResponseEntity.noContent().build(); // returns 204 No Content
    }


}