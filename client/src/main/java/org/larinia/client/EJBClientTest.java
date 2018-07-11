package org.larinia.client;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.larinia.ejb.CallerLocal;

public class EJBClientTest
{

   public static void main(String[] args)
   {
      new EJBClientTest().setProxy();
   }

   public void setProxy()
   {
      try
      {
         Properties contextProperties = new Properties();
         contextProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");

         Context context = new InitialContext(contextProperties);
         CallerLocal remoteService = (CallerLocal) context.lookup(
               "ejb:/TestServer-1/CallerBean!"+CallerLocal.class.getName());


         System.out.println("lookup done" + remoteService);
         System.out.println(remoteService.testMethod("blala"));
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }

   }

}
