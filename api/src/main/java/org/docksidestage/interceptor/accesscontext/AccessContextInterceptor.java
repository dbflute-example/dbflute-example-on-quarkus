package org.docksidestage.interceptor.accesscontext;

import java.time.LocalDateTime;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@AccessContext
@Interceptor
public class AccessContextInterceptor {

    @AroundInvoke
    public Object aroundInvoke(InvocationContext ctx) throws Exception {
        org.dbflute.hook.AccessContext context = new org.dbflute.hook.AccessContext();
        context.setAccessLocalDateTime(LocalDateTime.now());
        context.setAccessUser("TODO"); // TODO 実装する
        context.setAccessProcess("TODO");
        org.dbflute.hook.AccessContext.setAccessContextOnThread(context);

        return ctx.proceed();
    }
}
