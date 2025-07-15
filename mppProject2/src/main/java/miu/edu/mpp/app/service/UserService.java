package miu.edu.mpp.app.service;



import miu.edu.mpp.app.dto.user.UserLoginRequest;
import miu.edu.mpp.app.dto.user.UserLoginResponse;
import miu.edu.mpp.app.dto.user.UserRegisterRequest;

public interface UserService {
    UserLoginResponse login(UserLoginRequest request);
    UserLoginResponse register(UserRegisterRequest request);

}
