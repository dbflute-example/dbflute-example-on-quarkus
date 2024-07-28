package org.docksidestage.app.base;

import java.util.List;

import org.dbflute.Entity;
import org.dbflute.cbean.result.PagingResultBean;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * @author miyasama
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchPagingResult<BEAN> {

    /** row count per one page */
    public final int pageSize;

    /** number of current page */
    public final int currentPageNumber;

    /** count of all records */
    public final int allRecordCount;

    /** count of all pages */
    public final int allPageCount;

    /** paging data for current page */
    @NotNull
    @Valid
    public final List<BEAN> rows;

    public SearchPagingResult(PagingResultBean<? extends Entity> page, List<BEAN> rows) {
        this.pageSize = page.getPageSize();
        this.currentPageNumber = page.getCurrentPageNumber();
        this.allRecordCount = page.getAllRecordCount();
        this.allPageCount = page.getAllPageCount();
        this.rows = rows;
    }
}