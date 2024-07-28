package org.docksidestage.app.lido.mypage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.docksidestage.dbflute.exbhv.ProductBhv;

import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

/**
 * @author miyasama
 */
@Path("/mypage")
@Authenticated
@ApplicationScoped
public class MyPageController {

    // ===================================================================================
    //                                                                           Attribute
    //     
    @Inject
    ProductBhv productBhv;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/products")
    public List<MypageProductResult> index(@Context SecurityContext ctx) {
        var loginUserId = Optional.ofNullable(ctx.getUserPrincipal()).map(user -> Long.parseLong(user.getName())).orElse(null);
        System.out.println(loginUserId);
        //TODO Refine data by logged-in user ID

        var memberList = productBhv.selectList(cb -> {
            cb.query().addOrderBy_RegularPrice_Desc();
            cb.fetchFirst(3);
        });

        return memberList.stream().map(product -> {
            var result = new MypageProductResult();
            result.productName = product.getProductName();
            result.regularPrice = product.getRegularPrice();
            return result;
        }).collect(Collectors.toList());
    }
}