package com.redhat.client;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.jboss.logging.Logger;

@WebListener
public class ContextListener implements ServletContextListener {
    private static final Logger logger = Logger.getLogger(ContextListener.class);
   @Override
    public void contextInitialized(ServletContextEvent evt)  {
        ServletContext ctx = evt.getServletContext();
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@contextInitialized(): ServerInfo: " +
                ctx.getServerInfo() + "@@@@@@@@@@@@@@@@@ " + System.currentTimeMillis());
        System.out.println("contextInitialized(): ContextPath: " +
                ctx.getContextPath() + " " + System.currentTimeMillis());
        logger.info("contextInitialized(): ServerInfo: " +
                ctx.getServerInfo() + " " + System.currentTimeMillis());
        logger.info("contextInitialized(): ContextPath: " +
                ctx.getContextPath() + " " + System.currentTimeMillis());
    }

   @Override
    public void contextDestroyed(ServletContextEvent evt)  {
        ServletContext ctx = evt.getServletContext();
        System.out.println("contextDestroyed(): ServerInfo: " +
                ctx.getServerInfo() + " " + System.currentTimeMillis());
        System.out.println("contextDestroyed(): ContextPath: " +
                ctx.getContextPath() + " " + System.currentTimeMillis());
        logger.info("contextDestroyed(): ServerInfo: " +
                ctx.getServerInfo() + " " + System.currentTimeMillis());
        logger.info("contextDestroyed(): ContextPath: " +
                ctx.getContextPath() + " " + System.currentTimeMillis());
    }
}
