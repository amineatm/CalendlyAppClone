package miu.edu.mpp.app.service.impl;

import lombok.RequiredArgsConstructor;
import miu.edu.mpp.app.domain.User;
import miu.edu.mpp.app.dto.user.UserLoginRequest;
import miu.edu.mpp.app.dto.user.UserLoginResponse;
import miu.edu.mpp.app.dto.user.UserRegisterRequest;
import miu.edu.mpp.app.dto.user.UserResponse;
import miu.edu.mpp.app.error.exception.BusinessException;
import miu.edu.mpp.app.repository.UserRepository;
import miu.edu.mpp.app.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        User user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Invalid credentials"));

        UserResponse userResponse = UserResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .bio(user.getBio())
                .image(user.getImage())
                .token("IN BLANK")
                .build();

        return new UserLoginResponse(userResponse);
    }

    @Override
    public UserLoginResponse register(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already in use");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("Username already in use");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setBio("");
        user.setImage("");

        userRepository.save(user);

        UserResponse response = UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .image(user.getImage())
                .token("IN BLANK")
                .build();

        return new UserLoginResponse(response);
    }

}