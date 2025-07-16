package miu.edu.mpp.app.service.impl;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import miu.edu.mpp.app.domain.User;
import miu.edu.mpp.app.dto.user.*;
import miu.edu.mpp.app.error.exception.BusinessException;
import miu.edu.mpp.app.repository.UserRepository;
import miu.edu.mpp.app.security.JwtUtil;
import miu.edu.mpp.app.service.UserService;
import miu.edu.mpp.app.util.HashUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;


    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        User user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword()).orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Invalid credentials"));
        String token = jwtUtil.generateToken(user);

        UserResponse userResponse = UserResponse.builder().email(user.getEmail()).username(user.getUsername()).bio(user.getBio()).image(user.getImage()).token(token).build();

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

        UserResponse response = UserResponse.builder().username(user.getUsername()).email(user.getEmail()).bio(user.getBio()).image(user.getImage()).token(jwtUtil.generateToken(user)).build();

        return new UserLoginResponse(response);
    }

    @Override
    public UserRo findByEmail(String email) throws NotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
        return new UserRo(user.getUsername(), user.getEmail(), user.getBio(), user.getImage(), jwtUtil.generateToken(user));
    }

    @Override
    @Transactional
    public UserRo update(String email, UpdateUserRequest dto) throws NotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));

        if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
            user.setUsername(dto.getUsername());
        }
        if (dto.getBio() != null && !dto.getBio().isBlank()) {
            user.setBio(dto.getBio());
        }
        if (dto.getImage() != null && !dto.getImage().isBlank()) {
            user.setImage(dto.getImage());
        }
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(dto.getPassword());
        }

        userRepository.saveAndFlush(user);

        return new UserRo(user.getUsername(), user.getEmail(), user.getBio(), user.getImage(), jwtUtil.generateToken(user));
    }

    @Override
    public void delete(String email) {
        userRepository.deleteByEmail(email);
    }

}