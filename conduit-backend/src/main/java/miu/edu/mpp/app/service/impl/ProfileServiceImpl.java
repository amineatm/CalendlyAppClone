package miu.edu.mpp.app.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miu.edu.mpp.app.domain.User;
import miu.edu.mpp.app.dto.profile.ProfileDto;
import miu.edu.mpp.app.dto.profile.ProfileResponse;
import miu.edu.mpp.app.repository.UserRepository;
import miu.edu.mpp.app.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Override
    public ProfileResponse findProfile(Long followerId, String username) {
        User following = userRepository.findByUsernameWithFollowers(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User cannot be found"));

        User follower = entityManager.getReference(User.class, followerId);
        boolean isFollowing = true;//following.getFollowers().contains(follower);

        ProfileDto profile = new ProfileDto(following.getUsername(), following.getBio(), following.getImage(), isFollowing);
        return new ProfileResponse(profile);
    }

    @Override
    @Transactional
    public ProfileResponse follow(String followerEmail, String username) {
        if (followerEmail == null || username == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Follower email and username not provided.");
        }

        User following = userRepository.findByUsernameWithFollowers(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User to follow not found"));

        User follower = userRepository.findByEmail(followerEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Follower user not found"));

        if (follower.getEmail().equals(following.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot follow yourself.");
        }

        //follower.getFollowers().add(following);
        entityManager.flush();

        ProfileDto profile = new ProfileDto(following.getUsername(), following.getBio(), following.getImage(), true);
        return new ProfileResponse(profile);
    }

    @Override
    @Transactional
    public ProfileResponse unfollow(Long followerId, String username) {
        if (followerId == null || username == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "FollowerId and username not provided.");
        }

        User following = userRepository.findByUsernameWithFollowers(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User to unfollow not found"));

        User follower = entityManager.getReference(User.class, followerId);

        if (following.getId().equals(followerId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot unfollow yourself.");
        }

        //following.getFollowers().remove(follower);
        entityManager.flush();

        ProfileDto profile = new ProfileDto(following.getUsername(), following.getBio(), following.getImage(), false);
        return new ProfileResponse(profile);
    }
}


//getFollowers