package org.larinia.client;

import javax.annotation.Resource;

/**
 * Created by lyu on 10/08/16.
 */

import org.jboss.ejb.client.EJBClientInterceptor;
import org.jboss.ejb.client.EJBClientInvocationContext;


public class HelloClientInterceptor implements EJBClientInterceptor {

     @Resource
	private static EJBClientInvocationContext context;

    @Override
    public void handleInvocation(EJBClientInvocationContext context) throws Exception {
        context.getContextData().put("test_data", "it_works");

        // Must make this call
        context.sendRequest();
    }

    @Override
    public Object handleInvocationResult(EJBClientInvocationContext context)
            throws Exception {
        System.out.println("HelloClientInterceptor.handleInvocationResult" +context.getContextData().get("Testing"));
        // Must make this call
        return context.getResult();
    }



}