package org.larinia.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.RemoveException;
import java.rmi.RemoteException;

/**
 * Created by lyu on 09/08/16.
 */
public interface EJBTwoHome extends EJBHome {
    public EJBTwo create() throws CreateException, RemoteException;
    public EJBTwo findByTopic(String country, String language) throws RemoteException;
}
