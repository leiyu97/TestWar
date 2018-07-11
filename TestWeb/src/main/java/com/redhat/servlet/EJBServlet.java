package com.redhat.servlet;

import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Properties;

import com.redhat.ejb.server2server.EJBLookupFactory;
import org.larinia.ejb.CallerLocal;


/**
 * Created by lyu on 08/08/16.
 */
@WebServlet(value = "/EJBServlet")
public class EJBServlet extends HttpServlet {

    static protected CallerLocal callerLocal = null;
    protected Hashtable<?, ?> props;
    static protected String username ="lei";
    static protected String password="lei";
    static protected String host="totoro.usersys.redhat.com";
    static protected String port="8080";

    @EJB(lookup = "java:global/TestWar-1/CallerBean!org.larinia.ejb.CallerLocal")
    private org.larinia.ejb.CallerLocal local;

    @EJB(lookup = "java:global/TestWar-1/AntherEJBImpl!org.larinia.ejb.AnotherEJB")
    private org.larinia.ejb.AnotherEJB aejb;

    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter out = res.getWriter();
        out.println("org.larinia.ejb.CallerLocal local is: ####### " + local);
        out.println("########################");
        out.println("local.testMethod() is ###### " + local.testMethod("Lei Test EJB remote call"));
        out.println("local is " + local.ejbFindByTopic("testing123", "283424856562"));
        out.println("calling another ejb ######## " + aejb);
        out.println("method is in anothe ejb ###### " + aejb.testMyMethod());
        out.close();

        Properties props = new Properties();


        props.put(Context.INITIAL_CONTEXT_FACTORY,  "org.wildfly.naming.client.WildFlyInitialContextFactory");
        props.put(Context.PROVIDER_URL, "https-remoting://"+host+":"+port);



        props.put(Context.SECURITY_PRINCIPAL, username);
        props.put(Context.SECURITY_CREDENTIALS, password);

        try {
            CallerLocal cl = (CallerLocal)getAdapter();
            cl.testMethod(">>>>>>> ######## Using a different method");
        } catch (NamingException e) {
            e.printStackTrace();
        }


    }



    public CallerLocal getAdapter() throws NamingException {
        if(callerLocal == null) {

            System.out.println("EJBServlet.getAdapter Creating new  client stub.");

            callerLocal = EJBLookupFactory.createEjbRemotingWrapper(props);
        }
        return callerLocal;
    }

}
