package miu.edu.mpp.app.controller;


import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import miu.edu.mpp.app.dto.profile.ProfileResponse;
import miu.edu.mpp.app.error.ErrorMessage;
import miu.edu.mpp.app.security.CurrentUser;
import miu.edu.mpp.app.security.UserContext;
import miu.edu.mpp.app.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{username}")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable String username) throws NotFoundException {
        CurrentUser user = UserContext.get(); // current authenticated user
        ProfileResponse profile = profileService.getProfile(user.getId(), username);
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/{username}/follow")
    public ResponseEntity<ProfileResponse> follow(@PathVariable String username) {

        CurrentUser user = UserContext.get();

        return ResponseEntity.ok(profileService.follow(user, username));
    }

    @DeleteMapping("/{username}/follow")
    public ResponseEntity<ProfileResponse> unfollow(@PathVariable String username) {
        CurrentUser user = UserContext.get();
        return ResponseEntity.ok(profileService.unfollow(user, username));
    }
}