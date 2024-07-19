package org.docksidestage.app.lido.product;

import org.docksidestage.app.base.PagingForm;
import org.docksidestage.dbflute.allcommon.CDef;
import org.hibernate.validator.constraints.Length;

import jakarta.ws.rs.QueryParam;

/**
 * @author miyasama
 */
public class ProductSearchBody extends PagingForm {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @QueryParam("productName")
    @Length(max = 10)
    public String productName;

    @QueryParam("productStatus")
    public CDef.ProductStatus productStatus;

    @QueryParam("purchaseMemberName")
    @Length(max = 5)
    public String purchaseMemberName;
}
