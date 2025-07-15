package miu.edu.mpp.app.service;



import javassist.NotFoundException;
import miu.edu.mpp.app.dto.user.*;

public interface UserService {
    UserLoginResponse login(UserLoginRequest request);
    UserLoginResponse register(UserRegisterRequest request);
    UserRo findByEmail(String email) throws NotFoundException;
    UserRo update(String email, UpdateUserRequest dto) throws NotFoundException;
    void delete(String email);
}
