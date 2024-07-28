package org.docksidestage.app.lido.auth;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;

/**
 * @author miyasama
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SigninResult {

    @NotBlank
    public String jwt;
}
