package com.redhat.servlet;


import javax.naming.Context;
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



    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        int count=0;
        System.out.println("EJBServlet.service: just in "+count++);
        PrintWriter out = res.getWriter();


       // Properties props = new Properties();


       //props.put(Context.INITIAL_CONTEXT_FACTORY,  "org.wildfly.naming.client.WildFlyInitialContextFactory");
        //props.put(Context.PROVIDER_URL, "https-remoting://"+host+":"+port);



      //  props.put(Context.SECURITY_PRINCIPAL, username);
      //  props.put(Context.SECURITY_CREDENTIALS, password);
      CallerLocal cl = null;
        try {
            cl = (CallerLocal)getAdapter();
            cl.testMethod(">>>>>>> ######## Using a different method");
        } catch (NamingException e) {
            e.printStackTrace();
        }

        out.println("org.larinia.ejb.CallerLocal local is: ####### " );
        out.println("########################");
        out.println("local.testMethod() is ###### " + cl.testMethod("Lei Test EJB remote call"));
        out.println("local is " + cl.ejbFindByTopic("testing123", "283424856562"));
        out.println("calling another ejb ######## " );
        out.println("method is in anothe ejb ###### ");
        out.close();

    }



    public CallerLocal getAdapter() throws NamingException {
        if(callerLocal == null) {

            System.out.println("EJBServlet.getAdapter : just in ");

            callerLocal = EJBLookupFactory.createEjbRemotingWrapper(props);
        }
        return callerLocal;
    }

}
