package org.docksidestage.app.lido.product;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;

/**
 * @author miyasama
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductRowResult {

    public int productId;

    @NotBlank
    public String productName;

    @NotBlank
    public String productStatusName;

    public int regularPrice;
}
