package miu.edu.mpp.app.dto.user;

public class UserRo {
    private final String username;
    private final String email;
    private final String bio;
    private final String image;
    private final String token;

    public UserRo(String username, String email, String bio, String image, String token) {
        this.username = username;
        this.email = email;
        this.bio = bio;
        this.image = image;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getBio() {
        return bio;
    }

    public String getImage() {
        return image;
    }

    public String getToken() {
        return token;
    }
}
