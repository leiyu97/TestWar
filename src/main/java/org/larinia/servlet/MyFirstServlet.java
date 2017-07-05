package org.larinia.servlet; /**
 * Created by lyu on 15/06/16.
 */
//import org.jboss.security.JBossJSSESecurityDomain;

import com.redhat.spring.dao.PersonDAO;
import com.redhat.spring.model.Person;
import org.jboss.security.JBossJSSESecurityDomain;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.net.ssl.SSLContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

import java.security.cert.Certificate;

import java.util.List;
import java.util.Properties;

public class MyFirstServlet extends HttpServlet {
    private String message;
    KeyStore truststore;
    FileInputStream input;

   // private org.apache.log4j.Logger log4jlogger = org.apache.log4j.Logger.getLogger(this.getClass());

    //private Logger log = LoggerFactory.getLogger(this.getClass());
    public void init() throws ServletException {
        // Do required initialization
        message = "Hello World";
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {
        // Set response content type
        response.setContentType("text/html");

        // Actual logic goes here.
        PrintWriter out = response.getWriter();

        SSLContext context;
        Object o;
        printMultiTimes();
        //
       /* try {
            //getSSLContext();
             o = getSSLSecondMethod();

        } catch (NamingException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }*/

        //testSpring();
        //testing certs



      /* File trustore = new File(System.getProperty("javax.net.ssl.trustStore"));
        // use custom or default password (default: 'changeit')
        char[] truststorepass = System.getProperty("javax.net.ssl.trustStorePassword").toCharArray();

        File keystore = new File(System.getProperty("javax.net.ssl.keyStore"));
        char[] keystorepass = System.getProperty("javax.net.ssl.keyStorePassword").toCharArray();


        out.println("<h1> Keystore path is </h1>");
        out.println("<h2>"+keystore.getAbsolutePath()+ "</h2>");
        out.println("<h1> truststore path is </h1>");
        out.println("<h2>"+trustore.getAbsolutePath()+ "</h2>");

        try {
            input = new FileInputStream(trustore);
            truststore = KeyStore.getInstance(KeyStore.getDefaultType());
            truststore.load(input, truststorepass);


            input.close();

            //KeyStore ks = new FileSystemKeyStoreLoader(new File("path")).getKeyStore("pass");

        } catch (KeyStoreException kex) {
            kex.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            input.close();
        }
*/

        //     out.println("<h1>" + context.getDefaultSSLParameters().getServerNames().toString() + "</h1>");

        out.println("<h1>" + message + "</h1>");
        //log.debug("slf4j log from my First servlet");
    }

    public void destroy() {
        // do nothing.
    }

    private SSLContext getSSLContext() throws NamingException, GeneralSecurityException {
        Context ctx = new InitialContext();
        JBossJSSESecurityDomain sd = (JBossJSSESecurityDomain) ctx.lookup("java:jboss/jaas/CertificateDomain/jsse");

        if (sd == null) {
            System.out.println("the security domain is null ");
        }

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(sd.getKeyManagers(), sd.getTrustManagers(), null);
        try {

            KeyStore ks = sd.getKeyStore();
            KeyStore ts = sd.getTrustStore();



          /* while( ks.aliases().hasMoreElements()){

               System.out.println("MyFirstServlet.getSSLContext: keystore "+ks.aliases().nextElement());
           };*/


            //   for (Enumeration<String> aliases = ks.aliases(); aliases.hasMoreElements();)
            //      System.out.println("alias in keystores are: " +aliases.nextElement());


            Certificate privateCerts = ks.getCertificate("totoro.usersys.redhat.com");
            System.out.println("MyFirstServlet.getSSLContext: " + privateCerts.getPublicKey().toString());
            System.out.println("MyFirstServlet.getSSLContext: " + sd.getKeyStoreURL());


            System.out.println("MyFirstServlet.getSSLContext: truststore type is " + ts.getType());
            //  System.out.println("MyFirstServlet.getSSLContext: certificate is "+ct.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sslContext;
    }

    private Object getSSLSecondMethod() throws NamingException, GeneralSecurityException {
        Context ctx = new InitialContext();

        Object o = ctx.lookup("java:jboss/jaas/CertificateDomain/jsse");

        return o;
    }

   /* private SSLContext getSSLContext() throws NamingException, GeneralSecurityException {
        Context ctx = new InitialContext();
        JBossJSSESecurityDomain sd = (JBossJSSESecurityDomain)ctx.lookup("java:jboss/jaas/mysecuritydomain/jsse");

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(sd.getKeyManagers(), sd.getTrustManagers(), null);
        return sslContext;
    }*/

    private void testSpring() {


        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring4.xml");

        PersonDAO personDAO = context.getBean(PersonDAO.class);

        Person person = new Person();
        person.setName("lei");
        person.setCountry("GB");

        personDAO.save(person);

        System.out.println("Person::" + person);

        List<Person> list = personDAO.list();

        for (Person p : list) {
            System.out.println("Person List::" + p);
        }

        context.close();
    }

    public void printMultiTimes() {
        for (int count=0; count<=10; count++) {
            System.out.println("MyFirstServlet.printMultiTimes: print "+count);
        }
    }

    public void myMethod() throws IOException{

      /*  System.setProperty("javax.net.ssl.trustStore", "/home/vagrant/certs/truststore.jks");
       System.setProperty("javax.net.ssl.trustStorePassword", "12345678");
        System.setProperty("javax.net.ssl.keyStore", "/home/vagrant/certs/swallow.redhat.com.keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "12345678");
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
*/

        System.out.println("MyFirstServlet.myMethod : truststore is "+ System.getProperty("javax.net.ssl.trustStore"));
        System.out.println("MyFirstServlet.myMethod: keystore is "+System.getProperty("javax.net.ssl.keyStore"));

        URL jboss = new URL("https://totoro.usersys.redhat.com:8443");
        URLConnection yc = jboss.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        String inputLine;


        while ((inputLine = in.readLine()) != null)
        {
            System.out.println(inputLine);
        }


        in.close();


    }


}


