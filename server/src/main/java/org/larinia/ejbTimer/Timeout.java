package org.larinia.ejbTimer;

import javax.ejb.Remote;
import javax.ejb.Timer;

@Remote
public interface Timeout {
    public void initialize();
}
