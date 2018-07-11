package org.larinia.ejb;

import javax.ejb.Local;

/**
 * Created by svinayagam on 05/05/2017.
 */
@Local
public interface TestMBeanService
{
    String echo(String text);
    String deepEcho(String text);
}
