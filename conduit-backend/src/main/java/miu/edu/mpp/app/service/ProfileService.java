package miu.edu.mpp.app.service;

import javassist.NotFoundException;
import miu.edu.mpp.app.dto.profile.ProfileResponse;
import miu.edu.mpp.app.security.CurrentUser;

public interface ProfileService {
    ProfileResponse findProfile(Long followerId, String username);
    ProfileResponse follow(CurrentUser followerEmail, String username);
    ProfileResponse unfollow(CurrentUser userContext, String username);
    ProfileResponse getProfile(Long currentUserId, String username) throws NotFoundException;

}
