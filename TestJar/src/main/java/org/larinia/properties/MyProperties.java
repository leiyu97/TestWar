package org.larinia.properties;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Properties;
import javax.net.ssl.SSLContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;

/**
 * Created by lyu on 08/08/16.
 */
public class MyProperties extends HttpServlet {

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {
        // Set response content type
        response.setContentType("text/html");

        // Actual logic goes here.
        PrintWriter out = response.getWriter();


        try {
           // MyProperties properties = new MyProperties();
            String props = loadPropertiesFile("WEB-INF/classes/my.properties").getProperty("test");
            out.println("<h1>" + "using Input stream directly" + "</h1>");
            out.println("<h1>" + props + "</h1>");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
        }

    }
    public Properties loadPropertiesFile(String propertiesFile) throws Exception {

         InputStream is = getClass().getResourceAsStream(propertiesFile);
       // URL url = Thread.currentThread().getContextClassLoader().getResource(propertiesFile);

        /*if(url == null) {
            throw new Exception(propertiesFile + " does not exist");
        }

        InputStream is = url.openStream();
       */
        Properties properties = new Properties();
        properties.load(is);


        is.close();

        return properties;
    }
}
