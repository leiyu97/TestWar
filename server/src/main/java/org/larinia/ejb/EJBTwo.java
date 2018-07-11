package org.larinia.ejb;

import javax.ejb.EJBObject;
import java.rmi.Remote;

/**
 * Created by lyu on 09/08/16.
 */
public interface EJBTwo extends EJBObject {
    public Integer sum(Integer a, Integer b);
}
