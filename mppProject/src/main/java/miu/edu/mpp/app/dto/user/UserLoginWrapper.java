package miu.edu.mpp.app.dto.user;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class UserLoginWrapper {

    @NotNull
    @Valid
    private UserLoginRequest user;
}
