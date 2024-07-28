package org.docksidestage.app.lido.mypage;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;

/**
 * @author miyasama
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MypageProductResult {

    @NotBlank
    public String productName;

    public int regularPrice;
}
