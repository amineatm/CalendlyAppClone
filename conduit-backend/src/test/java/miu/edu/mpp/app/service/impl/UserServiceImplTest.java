package miu.edu.mpp.app.service.impl;

import javassist.NotFoundException;
import miu.edu.mpp.app.domain.User;
import miu.edu.mpp.app.dto.user.*;
import miu.edu.mpp.app.error.exception.BusinessException;
import miu.edu.mpp.app.repository.UserRepository;
import miu.edu.mpp.app.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_withValidCredentials_returnsToken() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("pass");
        user.setUsername("test");

        when(userRepository.findByEmailAndPassword("test@example.com", "pass"))
                .thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(user)).thenReturn("mock-token");

        UserLoginRequest request = new UserLoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("pass");
        UserLoginResponse response = userService.login(request);

        assertThat(response.getUser().getEmail()).isEqualTo("test@example.com");
        assertThat(response.getUser().getToken()).isEqualTo("mock-token");
    }

    @Test
    void login_withInvalidCredentials_throwsException() {
        when(userRepository.findByEmailAndPassword("nope", "wrong")).thenReturn(Optional.empty());
        UserLoginRequest ulr = new UserLoginRequest();
        ulr.setEmail("nope");
        ulr.setPassword("wrong");
        assertThatThrownBy(() -> userService.login(ulr))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining(UNAUTHORIZED.toString());
    }

    @Test
    void register_newUser_success() {
        UserRegisterRequest request = new UserRegisterRequest();

        request.setUsername("new");
        request.setEmail("new@example.com");
        request.setPassword("123");
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(userRepository.existsByUsername("new")).thenReturn(false);
        when(jwtUtil.generateToken(any())).thenReturn("new-token");

        UserLoginResponse response = userService.register(request);

        assertThat(response.getUser().getEmail()).isEqualTo("new@example.com");
        assertThat(response.getUser().getToken()).isEqualTo("new-token");
    }

    @Test
    void register_duplicateEmail_throwsException() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setUsername("user");
        request.setEmail("test@example.com");
        request.setPassword("123");
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.register(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Email already in use");
    }

    @Test
    void findByEmail_existingUser_returnsUserRo() throws NotFoundException {
        User user = new User();
        user.setUsername("user");
        user.setEmail("user@example.com");
        user.setBio("bio");
        user.setImage("img");

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(user)).thenReturn("tok");

        UserRo ro = userService.findByEmail("user@example.com");

        assertThat(ro.getEmail()).isEqualTo("user@example.com");
        assertThat(ro.getToken()).isEqualTo("tok");
    }

    @Test
    void update_existingUser_updatesFields() throws NotFoundException {
        User user = new User();
        user.setEmail("user@example.com");
        user.setUsername("old");
        user.setBio("bio");
        user.setImage("img");

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(any())).thenReturn("updated-token");

        UpdateUserRequest dto = new UpdateUserRequest();
        dto.setUsername("newName");
        dto.setBio("newBio");

        UserRo updated = userService.update("user@example.com", dto);

        assertThat(updated.getUsername()).isEqualTo("newName");
        assertThat(updated.getBio()).isEqualTo("newBio");
    }

    @Test
    void delete_existingUser_deletesByEmail() {
        userService.delete("del@example.com");
        verify(userRepository).deleteByEmail("del@example.com");
    }
}
