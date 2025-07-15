package miu.edu.mpp.app.controller;

import lombok.RequiredArgsConstructor;
import miu.edu.mpp.app.dto.user.UserLoginRequest;
import miu.edu.mpp.app.dto.user.UserLoginResponse;
import miu.edu.mpp.app.dto.user.UserLoginWrapper;
import miu.edu.mpp.app.dto.user.UserRegisterWrapper;
import miu.edu.mpp.app.service.UserService;
import static org.springframework.http.HttpStatus.CREATED;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRest {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@Valid @RequestBody UserLoginWrapper request) {
        return ResponseEntity.status(201).body(userService.login(request.getUser()));
    }

    @PostMapping
    public ResponseEntity<UserLoginResponse> register(@Valid @RequestBody UserRegisterWrapper body) {
        return ResponseEntity.status(CREATED).body(userService.register(body.getUser()));
    }
}