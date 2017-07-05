package org.larinia.ejb;

/**
 * Created by lyu on 10/08/16.
 */

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.annotation.Resource;
import javax.ejb.SessionContext;

public class HelloServerInterceptor {


    @AroundInvoke
    public Object log(InvocationContext invocationContext) throws Exception {
        String test_data = (String) invocationContext.getContextData().get("test_data");
        invocationContext.getContextData().put("Testing", "Lei");

        return invocationContext.proceed();
    }
}