package org.larinia.ejb;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.annotation.security.RunAs;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.jboss.security.annotation.SecurityDomain;

@Interceptors({HelloServerInterceptor.class})
@Stateless
//@RolesAllowed({"admin"})
//@SecurityDomain("ApplicationSecurityDomain")
//@SecurityDomain("jmx-console")
@Remote(CallerLocal.class)
public class CallerBean implements CallerLocal {
   // @RolesAllowed("admin")
    public String testMethod(String name) {
        System.out.println("nnt Bean's testMethod(String name) called....");
        return "[CallerBean] returned Hello " + name+"from the server side";
    }


    public Integer ejbFindByTopic(String company, String language) {
        System.out.println("CallerBean.ejbFindByTopic: 1");
        return 0;
    }

    public void ejbRemove() {
        System.out.println("CallerBean.ejbRemove: testing");
    }
}