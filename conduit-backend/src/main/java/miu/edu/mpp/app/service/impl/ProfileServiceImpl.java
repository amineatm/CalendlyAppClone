package miu.edu.mpp.app.service.impl;


import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miu.edu.mpp.app.domain.User;
import miu.edu.mpp.app.dto.profile.ProfileDto;
import miu.edu.mpp.app.dto.profile.ProfileResponse;
import miu.edu.mpp.app.repository.UserRepository;
import miu.edu.mpp.app.security.CurrentUser;
import miu.edu.mpp.app.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Override
    public ProfileResponse findProfile(Long followerId, String username) {
        User following = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User cannot be found"));

        User follower = entityManager.getReference(User.class, followerId);
        boolean isFollowing = true;//following.getFollowers().contains(follower);

        ProfileDto profile = new ProfileDto(following.getUsername(), following.getBio(), following.getImage(), isFollowing);
        return new ProfileResponse(profile);
    }

    /**
     * Following is the process of a user subscribing to another user's updates.
     * Follower is the user who wants to follow another user.
     * @param followingEmail
     * @param usernameFollower
     * @return
     */
    @Override
    @Transactional
    public ProfileResponse follow(CurrentUser followingEmail, String usernameFollower) {
        User following = userRepository.findByUsername(followingEmail.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User to follow not found"));

        if (following.getUsername().equals(usernameFollower)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot follow yourself.");
        }

        User follower = userRepository.findByUsername(usernameFollower)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Follower user not found"));

        // add the following user to the follower's followers list
//        var test = following.
        follower.getFollowers().add(following);
        userRepository.save(follower);

        ProfileDto profile = new ProfileDto(follower.getUsername(), follower.getBio(), follower.getImage(), true);
        return new ProfileResponse(profile);
    }

    @Override
    @Transactional
    public ProfileResponse unfollow(CurrentUser userContext, String username) {
        if (userContext == null || username == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current user and username must be provided.");
        }

        // Usuario logueado (yo, el que sigue)
        User currentUser = userRepository.findByUsername(userContext.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Logged in user not found"));

        // Usuario al que quiero dejar de seguir
        User toUnfollow = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User to unfollow not found"));

        if (currentUser.getId().equals(toUnfollow.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot unfollow yourself.");
        }

        List<User> test = currentUser.getFollowers();
        List<User> test2 = toUnfollow.getFollowers();
        System.out.println(entityManager.contains(currentUser)); // debería ser true

        // Aquí eliminamos la relación desde el lado propietario
        boolean removed = toUnfollow.getFollowers().removeIf(user -> user.getId().equals(currentUser.getId()));

        if (removed) {
//          userRepository.save(currentUser); // persistimos el cambio
            entityManager.merge(toUnfollow);
            entityManager.flush();
        }

        ProfileDto profile = new ProfileDto(toUnfollow.getUsername(), toUnfollow.getBio(), toUnfollow.getImage(), false);
        return new ProfileResponse(profile);
    }

    @Override
    public ProfileResponse getProfile(Long currentUserId, String username) throws NotFoundException {
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        User targetUser = userRepository.findByUsernameWithFollowers(username)
                .orElseThrow(() -> new NotFoundException("Target user not found"));

        boolean following = targetUser.getFollowers().contains(currentUser);

        ProfileDto profile = new ProfileDto(
                targetUser.getUsername(),
                targetUser.getBio(),
                targetUser.getImage(),
                following
        );
        return new ProfileResponse(profile);
    }

}


//getFollowers