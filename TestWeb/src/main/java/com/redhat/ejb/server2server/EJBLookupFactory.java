package com.redhat.ejb.server2server;

import org.larinia.ejb.CallerLocal;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;
import java.util.Properties;
import org.jboss.logging.Logger;

public class EJBLookupFactory {

    private static final String BEAN_NAME_CALLER="CallerBean";

    private static final String VIEW_CLASS_NAME =CallerLocal.class.getName();

    private static final String APP_NAME="TestServer-1";

    private static final Logger LOG = Logger.getLogger(EJBLookupFactory.class);


    private static Properties createJNDIProperties(Hashtable<?, ?> props) {
        Properties jndiProperties = new Properties();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        //lay added for eap 7.1
        //jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY,  "org.wildfly.naming.client.WildFlyInitialContextFactory");
        if (props != null) {
            jndiProperties.putAll(props);
        }
        System.out.println("EJBLookupFactory.createJNDIProperties getJNDIProperties mit erweiterten Properties: {}"+ jndiProperties);

        return jndiProperties;
    }

    private static String createWrapperJNDIName(String beanName, String viewClassName) {
        String jndiName = "ejb:/" + APP_NAME + "/" + beanName + "!" + viewClassName;
        System.out.println("EJBLookupFactory.createWrapperJNDIName Create JNDI Name: {}"+jndiName);

        return jndiName;
    }



    public static CallerLocal createEjbRemotingWrapper(Hashtable<?, ?> props) throws NamingException {
        Context context = null;
        try {
            final Properties jndiProperties = createJNDIProperties(props);

            context = new InitialContext(jndiProperties);

            String jndiName = createWrapperJNDIName(BEAN_NAME_CALLER, VIEW_CLASS_NAME);
            System.out.println("EJBLookupFactory.createEjbRemotingWrapper Looking EJB via JNDI => {}"+jndiName);
            Object o = context.lookup(jndiName);

            return (CallerLocal)o;
        } finally {
            if (context != null) {
                context.close();
            }
        }
    }


}
