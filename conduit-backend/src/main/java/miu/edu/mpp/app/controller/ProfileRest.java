package miu.edu.mpp.app.controller;


import lombok.RequiredArgsConstructor;
import miu.edu.mpp.app.dto.profile.ProfileResponse;
import miu.edu.mpp.app.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileRest {

    private final ProfileService profileService;

    @GetMapping("/{username}")
    public ResponseEntity<ProfileResponse> getProfile(@AuthenticationPrincipal Long userId,
                                                      @PathVariable String username) {
        return ResponseEntity.ok(profileService.findProfile(userId, username));
    }

    @PostMapping("/{username}/follow")
    public ResponseEntity<ProfileResponse> follow(@AuthenticationPrincipal String email,
                                                  @PathVariable String username) {
        return ResponseEntity.ok(profileService.follow(email, username));
    }

    @DeleteMapping("/{username}/follow")
    public ResponseEntity<ProfileResponse> unfollow(@AuthenticationPrincipal Long userId,
                                                    @PathVariable String username) {
        return ResponseEntity.ok(profileService.unfollow(userId, username));
    }
}