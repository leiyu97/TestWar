package org.larinia.ejb;


import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import org.jboss.logging.Logger;

@Startup
@Singleton
public class StartupBean
{
    private static final Logger logger = Logger.getLogger(StartupBean.class);
    @PostConstruct
    void startup() {
        System.out.println("startup()"+ "@PostContruct called");
        logger.warn("startup()"+ "@PostContruct called");
    }

    @PreDestroy
    void shutdown() {
        System.out.println("shutdown()"+ "@PreDestroy called");
        logger.warn("shutdown()"+ "@PreDestroy called");
    }
}
