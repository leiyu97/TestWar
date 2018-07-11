package org.larinia.servlet;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Created by lyu on 08/08/16.
 */
@WebServlet(value = "/EJBServlet")
public class EJBServlet extends HttpServlet {

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
    }


}
