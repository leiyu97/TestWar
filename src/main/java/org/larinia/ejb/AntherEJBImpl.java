package org.larinia.ejb;

import java.util.Hashtable;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Created by lyu on 08/08/16.
 */
@Stateless
@Remote(AnotherEJB.class)
public class AntherEJBImpl implements AnotherEJB {

    public String testMyMethod(){
        return getCallerLocal().testMethod("blagalkdfhakdgfgadfkgfakag");
    }

    public CallerLocal getCallerLocal() {
        InitialContext initialContext = null;
        CallerLocal cl = null;
        try {
            Hashtable<String, Object> env = new Hashtable<String, Object>();

            initialContext = new InitialContext(env);

            cl = (CallerLocal) initialContext.lookup("java:global/TestWar-1/CallerBean!org.larinia.ejb.CallerLocal");
            //this is jboss async, this is fine I think
            //org.jboss.ejb3.common.proxy.plugins.async.AsyncUtils.mixinAsync(home);

        } catch (NamingException e) {
            e.printStackTrace();
        }

        return cl;
    }
}
