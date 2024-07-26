package org.docksidestage.app.lido.auth;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.docksidestage.dbflute.exbhv.MemberBhv;
import org.docksidestage.dbflute.exbhv.MemberLoginBhv;
import org.docksidestage.dbflute.exbhv.MemberSecurityBhv;
import org.docksidestage.dbflute.exbhv.MemberServiceBhv;
import org.docksidestage.dbflute.exentity.Member;
import org.docksidestage.dbflute.exentity.MemberLogin;
import org.docksidestage.interceptor.accesscontext.AccessContext;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.jwt.JsonWebToken;

import io.quarkus.security.AuthenticationFailedException;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * @author miyasama
 */
@Path("/auth")
@ApplicationScoped
public class AuthController {

    // ===================================================================================
    //                                                                           Attribute
    //     
    @Inject
    MemberBhv memberBhv;

    @Inject
    MemberSecurityBhv memberSecurityBhv;

    @Inject
    MemberServiceBhv memberServiceBhv;

    @Inject
    MemberLoginBhv memberLoginBhv;

    @Inject
    JsonWebToken jwt;

    @Inject
    JWTParser jwtParser;

    @Inject
    Config config;

    @POST
    @Path("/signin")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @AccessContext
    public SigninResult search(@Valid SigninBody body) {
        var member = memberBhv.selectEntity(cb -> {
            cb.query().arrangeLogin(body.account, encryptPassword(body.password));
        }).orElseThrow(() -> new AuthenticationFailedException());

        insertLogin(member);

        var jwt = issueJwt(member);

        var result = new SigninResult();
        result.jwt = jwt;
        return result;
    }

    private String encryptPassword(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(value.getBytes());
            return String.format("%064x", new BigInteger(1, md.digest()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("パスワードの暗号化に失敗しました。", e);
        }
    }

    private String issueJwt(Member member) {
        return Jwt.issuer(config.getValue("mp.jwt.verify.issuer", String.class))
                .expiresAt(LocalDateTime.now().plusMonths(1).toInstant(ZoneOffset.UTC)) // 有効期限を30日後に設定
                .upn(member.getMemberId().toString()) //
                .claim("name", member.getMemberName())
                .sign();
    }

    private void insertLogin(Member member) {
        MemberLogin login = new MemberLogin();
        login.setMemberId(member.getMemberId());
        login.setLoginMemberStatusCodeAsMemberStatus(member.getMemberStatusCodeAsMemberStatus());
        login.setLoginDatetime(LocalDateTime.now());
        login.setMobileLoginFlg_False();
        memberLoginBhv.insert(login);
    }
}