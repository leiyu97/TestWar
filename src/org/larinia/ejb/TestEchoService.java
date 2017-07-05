package org.larinia.ejb;

import javax.ejb.Local;

/**
 * Created by svinayagam on 05/05/2017.
 */
@Local
public interface TestEchoService
{
    String echo(String text);
}
