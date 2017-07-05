package org.larinia.ejb;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import java.rmi.RemoteException;


/**
 * Created by lyu on 09/08/16.
 */
public class EJBTwoBean implements SessionBean {

    @Override
    public void setSessionContext(SessionContext sessionContext) throws EJBException, RemoteException {

    }

    @Override
    public void ejbRemove() throws EJBException, RemoteException {
        System.out.println("EJBTwoBean.ejbRemove: doing nothing");
    }

    @Override
    public void ejbActivate() throws EJBException, RemoteException {
        System.out.println("EJBTwoBean.ejbActivate: doing nothing");
    }

    @Override
    public void ejbPassivate() throws EJBException, RemoteException {
        System.out.println("EJBTwoBean.ejbPassivate: doing nothing");
    }

    public Integer sum(Integer a, Integer b) {
        return a+b;
    }

    public void ejbCreate() {

    }

    public Integer ejbFindByTopic() {
        return 90;
    }
}
