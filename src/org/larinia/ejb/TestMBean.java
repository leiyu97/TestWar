/* Copyright (c) 2013 Commonwealth of Australia.  All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * the Commonwealth of Australia. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with
 * the Commonwealth of Australia.
 */

package org.larinia.ejb;

//import au.gov.aec.genesis.infrastructure.business.service.BaseMBeanSession;
//import au.gov.aec.genesis.infrastructure.business.service.JmxBean;
//import au.gov.aec.genesis.infrastructure.business.service.JmxOperation;
//import au.gov.aec.genesis.infrastructure.business.service.JmxParam;
//import au.gov.aec.genesis.security.interfaces.util.SecurityConstant;
import org.jboss.ejb3.annotation.RunAsPrincipal;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

//import static au.gov.aec.genesis.infrastructure.interfaces.util.InfrastructureConstant.SYSTEM_USERNAME;
//import static au.gov.aec.genesis.security.interfaces.util.SecurityConstant.DEFAULT_SECURITY_ROLE;

@Startup
@Singleton
@JmxBean
public class TestMBean
        extends
        BaseMBeanSession
{
    @EJB
    private TestMBeanService testMBeanService;

    @JmxOperation(desc = "Echo a string to test a JMX operation")
    public String echo(@JmxParam(desc = "String to echo") String text)
    {
        return testMBeanService.echo(text);
    }

    @JmxOperation(desc = "Deep echo from Session bean.")
    public String deepEcho(@JmxParam(desc = "String to deepEcho") String text)
    {
        return testMBeanService.deepEcho(text);
    }

}
