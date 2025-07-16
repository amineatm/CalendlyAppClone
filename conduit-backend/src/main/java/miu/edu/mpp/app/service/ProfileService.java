package miu.edu.mpp.app.service;

import javassist.NotFoundException;
import miu.edu.mpp.app.dto.profile.ProfileResponse;

public interface ProfileService {
    ProfileResponse findProfile(Long followerId, String username);
    ProfileResponse follow(String followerEmail, String username);
    ProfileResponse unfollow(Long followerId, String username);
    ProfileResponse getProfile(Long currentUserId, String username) throws NotFoundException;

}
