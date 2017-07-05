package org.larinia.client;

//import com.nyiso.emt.common.config.JndiValues;
//import com.nyiso.emt.test.common.util.Tail;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.logging.Logger;
import org.larinia.ejb.CallerLocal;


public final class EjbWrapper<T> implements AutoCloseable {
    private static final Logger LOG = Logger.getLogger(EjbWrapper.class);
    private static final String LOCAL_INTERFACE_SUFFIX = "Local";
    private static final String REMOTE_INTERFACE_SUFFIX = "Remote";
    private T bean = null;
    private Context ejbRootNamingContext = null;
    private Context rootContext = null;

    private static InitialContext ctx;

    private EjbWrapper(T bean, Context rootContext, Context ejbRootNamingContext) {
        this.bean = bean;
        this.rootContext = rootContext;
        this.ejbRootNamingContext = ejbRootNamingContext;
    }

    public void close() {
        if (this.ejbRootNamingContext != null) {
            try {
                this.ejbRootNamingContext.close();
            } catch (NamingException var2) {
                LOG.error("Error closing EJB Root Naming Context", var2);
            }
        }

    }

    public T getBean() {
        return this.bean;
    }

    public static <T> EjbWrapper<T> lookup(Class<T> clazz, String username, String password) throws NamingException {
        return lookup(clazz, "TestWar-1", username, password);
    }

    public static <T> EjbWrapper<T> lookup(Class<T> clazz, String applicationName, String username, String password) throws NamingException {
        Properties props = new Properties();
        /*props.put("remote.connections", "default");
        props.put("remote.connection.default.port", "4447");
        props.put("remote.connection.default.host", "totoro.usersys.redhat.com");
        props.put("remote.connection.default.connect.options.org.xnio.Options.SASL_DISALLOWED_MECHANISMS", "JBOSS-LOCAL-USER");
        props.put("org.jboss.ejb.client.scoped.context", "true");*/

        //working props below

        props.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
        props.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");

        props.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "false");
        if (username != null) {
            props.put("remote.connection.default.username", username);
            if (password != null) {
                props.put("remote.connection.default.password", password);
            }
        } else {
            props.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "false");
        }

       props.put("java.naming.factory.url.pkgs", "org.jboss.ejb.client.naming");

        props.put("jboss.naming.client.ejb.context", "true");
        props.put("java.naming.factory.initial", "org.jboss.naming.remote.client.InitialContextFactory");
        props.put("java.naming.provider.url", "remote://totoro.usersys.redhat.com:4447");


        InitialContext rootContext = new InitialContext(props);
        String jndiName = computeJNDIName(clazz, applicationName);
        //"java:global/" +
        Object bean = rootContext.lookup( jndiName);
        return new EjbWrapper(clazz.cast(bean), rootContext, (Context) null);
    }

    private static <T> String computeJNDIName(Class<T> clazz, String applicationName) {
        String moduleName = clazz.getSimpleName();
       if (moduleName.endsWith("Local")) {
            moduleName = moduleName.substring(0, moduleName.length() - "Local".length());
        } else if (moduleName.endsWith("Remote")) {
            moduleName = moduleName.substring(0, moduleName.length() - "Remote".length());
        }

        String tempString = applicationName + "/" + moduleName + "Bean!" + clazz.getName();
        System.out.println("EjbWrapper.computeJNDIName: " + tempString);
        return tempString;
    }

    public static void main(String[] args) {

        CallerLocal callerLocal = null;


        try {
            EjbWrapper<CallerLocal> ejbWrapper = lookup(CallerLocal.class, "lei", "lei");
            //callerLocal = (CallerLocal)
            callerLocal = ejbWrapper.getBean();
            String testString = callerLocal.testMethod("testing Interceptor");
            System.out.println("EjbWrapper.main returned string :"+testString);
        } catch (NamingException e) {
            e.printStackTrace();
        }


        // testInterceptedEjb();

    }

    public static void testInterceptedEjb() {

        BufferedReader brConsoleReader = null;
        try {

            String jndiString = "java:global/TestWar-1/CallerBean!org.larinia.ejb.CallerLocal";

            CallerLocal callerLocal = (CallerLocal) ctx.lookup(jndiString);

            callerLocal.testMethod("Lei testing Interceptor");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (brConsoleReader != null) {
                    brConsoleReader.close();
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
