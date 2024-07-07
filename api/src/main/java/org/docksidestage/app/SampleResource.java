package org.docksidestage.app;

import org.docksidestage.dbflute.exbhv.MemberBhv;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/sample")
@ApplicationScoped
public class SampleResource {

    @Inject
    MemberBhv memberBhv;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return memberBhv.selectByPK(1).get().getMemberName();
    }
}