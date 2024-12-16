package vn.bachdao.soundcloud.web.rest.vm;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * View Model object for storing a user's credentials.
 */
@Getter
@Setter
public class LoginVM {
    @NotBlank(message = "Username không được để trống")
    @Size(min = 1, max = 50)
    private String username;

    @NotBlank(message = "Password không được để trống")
    @Size(min = 4, max = 100)
    private String password;
}
