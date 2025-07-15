package miu.edu.mpp.app.dto.user;


import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class UserRegisterWrapper {
    @NotNull
    @Valid
    private UserRegisterRequest user;
}