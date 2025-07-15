package miu.edu.mpp.app.dto.profile;

public class ProfileResponse {
    private ProfileDto profile;

    public ProfileResponse() {}

    public ProfileResponse(ProfileDto profile) {
        this.profile = profile;
    }

    public ProfileDto getProfile() {
        return profile;
    }

    public void setProfile(ProfileDto profile) {
        this.profile = profile;
    }
}
