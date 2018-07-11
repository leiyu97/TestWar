package org.larinia.ejb;

/**
 * Created by lyu on 08/08/16.
 */

import javax.ejb.Remote;
@Remote
public interface AnotherEJB {
    public String testMyMethod();
}
