package miu.edu.mpp.app.service.impl;

import miu.edu.mpp.app.domain.User;
import miu.edu.mpp.app.dto.profile.ProfileResponse;
import miu.edu.mpp.app.repository.UserRepository;
import miu.edu.mpp.app.security.CurrentUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfileServiceImplTest {

    private UserRepository userRepository;
    private EntityManager entityManager;
    private ProfileServiceImpl profileService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        entityManager = mock(EntityManager.class);
        profileService = new ProfileServiceImpl(userRepository, entityManager);
    }

    @Test
    void follow_shouldAddFollower() {
        // Setup
        CurrentUser currentUser = new CurrentUser(1L, "user1@example.com", "user1");

        User followingUser = new User();
        followingUser.setId(1L);
        followingUser.setUsername("user1");
        followingUser.setFollowers(new ArrayList<>());

        User targetUser = new User();
        targetUser.setId(2L);
        targetUser.setUsername("user2");
        targetUser.setFollowers(new ArrayList<>());

        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(followingUser));
        when(userRepository.findByUsername("user2")).thenReturn(Optional.of(targetUser));

        // Act
        ProfileResponse response = profileService.follow(currentUser, "user2");

        // Assert
        assertEquals("user2", response.getProfile().getUsername());
        assertTrue(response.getProfile().isFollowing());
        verify(userRepository).save(targetUser);
    }

    @Test
    void unfollow_shouldRemoveFollower() {
        // Setup
        CurrentUser currentUser = new CurrentUser(1L, "user1@example.com", "user1");

        User current = new User();
        current.setId(1L);
        current.setUsername("user1");

        User toUnfollow = new User();
        toUnfollow.setId(2L);
        toUnfollow.setUsername("user2");

        ArrayList<User> followers = new ArrayList<>();
        followers.add(current);
        toUnfollow.setFollowers(followers);

        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(current));
        when(userRepository.findByUsername("user2")).thenReturn(Optional.of(toUnfollow));

        // Act
        ProfileResponse response = profileService.unfollow(currentUser, "user2");

        // Assert
        assertEquals("user2", response.getProfile().getUsername());
        assertFalse(response.getProfile().isFollowing());
        verify(entityManager).merge(toUnfollow);
        verify(entityManager).flush();
    }
}
