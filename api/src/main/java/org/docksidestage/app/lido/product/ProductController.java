package org.docksidestage.app.lido.product;

import java.util.stream.Collectors;

import org.dbflute.cbean.result.PagingResultBean;
import org.docksidestage.app.base.SearchPagingResult;
import org.docksidestage.dbflute.exbhv.ProductBhv;
import org.docksidestage.dbflute.exentity.Product;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * @author miyasama
 */
@Path("/products")
@ApplicationScoped
public class ProductController {

    // ===================================================================================
    //                                                                           Attribute
    //     
    @Inject
    ProductBhv productBhv;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public SearchPagingResult<ProductRowResult> search(@BeanParam @Valid ProductSearchBody form) {
        var page = selectProductPage(form.page, form);
        var items = page.stream().map(product -> {
            return mappingToBean(product);
        }).collect(Collectors.toList());

        return new SearchPagingResult<>(page, items);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{productId}")
    public ProductRowResult product(@PathParam("productId") @NotNull Integer productId) {
        var product = selectProduct(productId);
        return mappingToBean(product);
    }

    private PagingResultBean<Product> selectProductPage(int pageNumber, ProductSearchBody form) {
        return productBhv.selectPage(cb -> {
            cb.setupSelect_ProductStatus();
            cb.setupSelect_ProductCategory();
            cb.specify().derivedPurchase().count(purchaseCB -> {
                purchaseCB.specify().columnPurchaseId();
            }, Product.ALIAS_purchaseCount);
            if (form.productName != null && !form.productName.isBlank()) {
                cb.query().setProductName_LikeSearch(form.productName, op -> op.likeContain());
            }
            if (form.purchaseMemberName != null && !form.purchaseMemberName.isBlank()) {
                cb.query().existsPurchase(purchaseCB -> {
                    purchaseCB.query().queryMember().setMemberName_LikeSearch(form.purchaseMemberName, op -> op.likeContain());
                });
            }
            if (form.productStatus != null) {
                cb.query().setProductStatusCode_Equal_AsProductStatus(form.productStatus);
            }
            cb.query().addOrderBy_ProductName_Asc();
            cb.query().addOrderBy_ProductId_Asc();
            cb.paging(form.pageSize, pageNumber);
        });
    }

    private Product selectProduct(int productId) {
        return productBhv.selectEntity(cb -> {
            cb.setupSelect_ProductStatus();
            cb.setupSelect_ProductCategory();
            cb.query().setProductId_Equal(productId);
        }).get();
    }

    private ProductRowResult mappingToBean(Product entity) {
        ProductRowResult response = new ProductRowResult();
        response.productId = entity.getProductId();
        response.productName = entity.getProductName();
        entity.getProductStatus().alwaysPresent(status -> {
            response.productStatusName = status.getProductStatusName();
        });
        response.regularPrice = entity.getRegularPrice();
        return response;
    }
}