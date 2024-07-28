package org.docksidestage.app.base;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.QueryParam;

/**
 * @author miyasama
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PagingForm {

    @QueryParam("page")
    @DefaultValue(value = "1")
    @Min(value = 1)
    public Integer page;

    @QueryParam("page_size")
    @DefaultValue(value = "50")
    @Min(value = 1)
    @Max(value = 100)
    public Integer pageSize;
}