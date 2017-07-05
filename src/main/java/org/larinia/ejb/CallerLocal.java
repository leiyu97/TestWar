package org.larinia.ejb;

/**
 * Created by lyu on 08/08/16.
 */


import javax.ejb.*;

@Remote
public interface CallerLocal {
    public String testMethod(String name);

    public Integer ejbFindByTopic(String company, String language);
}

