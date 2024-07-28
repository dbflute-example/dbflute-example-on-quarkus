package org.docksidestage.app.lido.auth;

import jakarta.validation.constraints.NotBlank;

/**
 * @author miyasama
 */
public class SigninBody {

    @NotBlank
    public String account;

    @NotBlank
    public String password;
}
