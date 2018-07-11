/* 
 *  ING-DiBa BPE-Wrapper
 * 
 *  Copyright (C) 2015 by ING-DiBa. All rights reserved.
 */
package com.redhat.ejb.server2server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import org.jboss.logging.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ing.diba.bpe.BpeConstants;
import com.ing.diba.bpe.api.BPEAdapter;
import com.ing.diba.bpe.api.BPEAdminAdapter;
import com.ing.diba.bpe.remoting.ejb.BPEAdapterHome;
import com.ing.diba.bpe.remoting.ejb.BPEAdapterRemote;
import com.ing.diba.bpe.remoting.ejb.BPEAdminAdapterHome;
import com.ing.diba.bpe.remoting.ejb.BPEAdminAdapterRemote;
import com.ing.diba.bpe.remoting.ejb.BPERemoteEJBOject;

/**
 * Factory zum Erzeugen von BPE-Adaptern.
 * 
 * @author David Wanaki
 * @since 28.10.2014
 */
public class BPEAdapterServiceFactory {
  
  /** This class' logger. */
  private static final Logger LOG = Logger.getLogger(BPEAdapterServiceFactory.class);
  
 
  //BPE Wrapper auf JBoss EAP 6
  private static final String APP_NAME_CAMUNDA = "BPE-Wrapper";
  private static final String MODULE_NAME_CAMUNDA = "BPE-Wrapper.jar";
  private static final String BEAN_NAME_ADMIN_CAMUNDA = "BPEAdapterAdminBean";
  private static final String BEAN_NAME_CAMUNDA = "BPEAdapterBean";
  
  
  private static Boolean inJBossEAP6 = null;
  private static boolean unitTestMode = false;
  
  /**
   * Befindet sich der Client im Test Modus? Default ist: false
   * 
   * @return Liefert true oder false
   */
  protected static boolean isUnitTestMode() {
    return unitTestMode;
  }
  
  /**
   * Setzt den JUnit Test Modus fuer den Client. Default ist: false
   * 
   * @param unitTestMode
   *          Setze true oder false
   * 
   * @see BpeConstants#JUNIT_USE_JBOSS_5_INSTANCE
   */
  protected static void setUnitTestMode(boolean unitTestMode) {
    BPEAdapterServiceFactory.unitTestMode = unitTestMode;
    inJBossEAP6 = isInJBossEAP6Internal();
  }
  
  static {
    inJBossEAP6 = isInJBossEAP6Internal();
  }
  
  private BPEAdapterServiceFactory() {
    // Sonar will das so
  }
  
  protected static boolean isInJBossEAP6() {
    return inJBossEAP6;
  }
  
  private static boolean isInJBossEAP6Internal() {
    // Wenn JUnit Test Mode gesetzt und property, dann JBoss EAP 5 verwenden
    if (isUnitTestMode()) {
      String val = "";
      try {
        val = System.getProperty(BpeConstants.JUNIT_USE_JBOSS_5_INSTANCE, "false");
      } catch (RuntimeException e) {
        LOG.error("JUnit Test Mode is set, but an error occured resoliving the property " + BpeConstants.JUNIT_USE_JBOSS_5_INSTANCE,
                  e);
      }
      if ("true".equals(val)) {
        LOG.info("JUnit Test Mode and property {} is set. Using JBoss EAP 5." + BpeConstants.JUNIT_USE_JBOSS_5_INSTANCE);
        return false;
      }
    }
    
    // JBoss EAP 6
    MBeanServer server = findJBossMBeanServer();
    if (server == null) {
      LOG.warn("No JBoss-MBeanServer found - unmanaged environment, assuming JBoss EAP 6");
      return true;
    }
    
    // JBoss EAP 5
    try {
      try {
        ObjectName name = new ObjectName("jboss.system:type=Server");
        String version = (String) (server.getAttribute(name, "Version"));
        LOG.debug("Running JBoss VERSION: {}"+ version);

        return false;
      } catch (InstanceNotFoundException e) {
        ObjectName name = new ObjectName("jboss.as:management-root=server");
        String version = (String) (server.getAttribute(name, "releaseVersion"));
        LOG.debug("Running JBoss VERSION: {}"+ version);

        return true;
      }
    } catch (Exception e) {
      LOG.error("Error during JBoss Version determination: ", e);
      LOG.warn("Running JBoss VERSION could not be determined, assuming JBoss EAP 6");
      return true;
    }
  }
  
  protected static BPEAdminAdapter createEjbRemotingBPEAdminWrapper(Hashtable<?, ?> props) throws NamingException {
    Context context = null;
    try {
      final Properties jndiProperties = createJNDIProperties(props);
      
      context = new InitialContext(jndiProperties);
      
      String jndiName = createBPEWrapperJNDIName(BEAN_NAME_ADMIN_CAMUNDA, BPEAdminAdapter.class.getName());
      LOG.debug("Looking EJB via JNDI => {}"+ jndiName);

      return (BPEAdminAdapter) context.lookup(jndiName);
    } finally {
      if (context != null) {
        context.close();
      }
    }
  }
  
  protected static BPEAdapter createEjbRemotingBPEWrapper(Hashtable<?, ?> props) throws NamingException {
    Context context = null;
    try {
      final Properties jndiProperties = createJNDIProperties(props);
      
      context = new InitialContext(jndiProperties);
      
      String jndiName = createBPEWrapperJNDIName(BEAN_NAME_CAMUNDA, BPEAdapter.class.getName());
      LOG.debug("Looking EJB via JNDI => {}"+ jndiName);

      return (BPEAdapter) context.lookup(jndiName);
    } finally {
      if (context != null) {
        context.close();
      }
    }
  }
  
 
  private static MBeanServer findJBossMBeanServer() {
    List<MBeanServer> servers = javax.management.MBeanServerFactory.findMBeanServer(null);
    for (MBeanServer jboss : servers) {
      Set<String> domains = new HashSet<String>(Arrays.asList(jboss.getDomains()));
      if (domains.contains("jboss.as") || domains.contains("jboss.system")) {
        return jboss;
      }
    }
    return null;
  }
  
  private static Properties createJNDIProperties(Hashtable<?, ?> props) {
    Properties jndiProperties = new Properties();
    jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    
    if (props != null) {
      jndiProperties.putAll(props);
    }
    LOG.debug("getJNDIProperties mit erweiterten Properties: {}"+ jndiProperties);

    return jndiProperties;
  }
  
  private static String createBPEWrapperJNDIName(String beanName, String viewClassName) {
    String jndiName = "ejb:" + APP_NAME_CAMUNDA + "/" + MODULE_NAME_CAMUNDA + "//" + beanName + "!" + viewClassName;
    LOG.debug("Create JNDI Name: {}"+jndiName);

    return jndiName;
  }
  
  private static RemoteInvocationEJBHandler createBPEAdminAdapterInvocationEJBHandler(Hashtable<?, ?> props, String homeJndiName)
      throws NamingException, CreateException, RemoteException {
    if (props == null) {
      throw new IllegalArgumentException("environment properties must not be null");
    }
    
    RemoteInvocationEJBHandler handler = new BPEAdminAdapterInvocationEJBHandler(props, homeJndiName);
    handler.initialize();
    return handler;
  }
  
  private static RemoteInvocationEJBHandler createBPEAdapterInvocationEJBHandler(Hashtable<?, ?> props, String homeJndiName)
      throws NamingException, CreateException, RemoteException {
    if (props == null) {
      throw new IllegalArgumentException("environment properties must not be null");
    }
    
    RemoteInvocationEJBHandler handler = new BPEAdapterInvocationEJBHandler(props, homeJndiName);
    handler.initialize();
    return handler;
  }
  
  // Klassen fuer die Aufrufe des alten BPE-Adapters; hier 1:1 uebernommen
  
  private static class BPEAdminAdapterInvocationEJBHandler extends RemoteInvocationEJBHandler {
    
    private BPEAdminAdapterHome home;
    
    BPEAdminAdapterInvocationEJBHandler(Hashtable<?, ?> props, String jndiName) {
      super(props, jndiName);
    }
    
    @Override
    public void narrowHome(Object ref) {
      home = (BPEAdminAdapterHome) PortableRemoteObject.narrow(ref, BPEAdminAdapterHome.class);
    }
    
    @Override
    public BPERemoteEJBOject createBean() throws RemoteException, CreateException {
      return home.create();
    }
    
  }
  
  private static class BPEAdapterInvocationEJBHandler extends RemoteInvocationEJBHandler {
    
    private BPEAdapterHome home;
    
    BPEAdapterInvocationEJBHandler(Hashtable<?, ?> props, String jndiName) {
      super(props, jndiName);
    }
    
    @Override
    public void narrowHome(Object ref) {
      home = (BPEAdapterHome) PortableRemoteObject.narrow(ref, BPEAdapterHome.class);
    }
    
    @Override
    public BPERemoteEJBOject createBean() throws RemoteException, CreateException {
      return home.create();
    }
    
  }
  
  private static class EAP6BPEAdapterInvocationEJBHandler implements java.lang.reflect.InvocationHandler {
    
    private BPERemoteEJBOject bean;
    
    EAP6BPEAdapterInvocationEJBHandler(BPERemoteEJBOject bean) {
      this.bean = bean;
    }
    
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
      Object result = null;
      try {
        Invocation inv = new Invocation(m, args);
        InvocationResult invResult = bean.invoke(inv);
        result = invResult.getResult();
      } catch (InvocationTargetException e) {
        LOG.error("invocation failed: ", e);
        throw e;
      } catch (EJBException re) {
        Throwable cause = re.getCause();
        LOG.error("adapter call failed: ", cause);
        throw cause;
      }
      return result;
    }
  }
  
  private static abstract class RemoteInvocationEJBHandler implements java.lang.reflect.InvocationHandler {
    
    private Hashtable<?, ?> props;
    private String jndiName;
    
    private BPERemoteEJBOject bean;
    private InitialContext ctxt;
    
    private static final Logger LOG = LoggerFactory.getLogger(RemoteInvocationEJBHandler.class);
    
    void initialize() throws NamingException, CreateException, RemoteException {
      ctxt = new InitialContext(props);
      LOG.debug("RemoteInvocationEJBHandler.initialize : Create JNDI Name: {}"+jndiName);

      Object ref = ctxt.lookup(jndiName);
      narrowHome(ref);
      connect();
    }
    
    protected abstract void narrowHome(Object ref);
    
    void connect() throws CreateException, RemoteException {
      bean = createBean();
    }
    
    protected abstract BPERemoteEJBOject createBean() throws RemoteException, CreateException;
    
    RemoteInvocationEJBHandler(Hashtable<?, ?> props, String jndiName) {
      this.props = props;
      this.jndiName = jndiName;
    }
    
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
      Object result = null;
      
      try {
        Invocation inv = new Invocation(m, args);
        InvocationResult invResult = bean.invoke(inv);
        result = invResult.getResult();
      } catch (InvocationTargetException e) {
        LOG.error("invocation failed: ", e);
        throw e;
      } catch (RemoteException re) {
        Throwable cause = re.getCause();
        LOG.error("adapter call failed: ", cause);
        throw cause;
      }
      return result;
    }
  }
}
