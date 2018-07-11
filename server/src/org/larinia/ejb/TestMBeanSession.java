/* Copyright (c) 2008 Commonwealth of Australia.  All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * the Commonwealth of Australia. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with
 * the Commonwealth of Australia.
 */

package org.larinia.ejb;

import au.gov.aec.genesis.security.business.service.SecureBaseSession;
import org.jboss.ejb3.annotation.RunAsPrincipal;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.security.SecurityDomain;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
@SecurityDomain("genesis-system-user")
@RunAs("Grp_Genesis_Users")
@RunAsPrincipal(value = "system")
@PermitAll
public class TestMBeanSession
        extends
        SecureBaseSession implements TestMBeanService
{
    @EJB
    private TestEchoService echoService;

    public String echo(String text)
    {
        String str = "TestMBeanSession says: " + text;
        return str;
    }

    @Override
    public String deepEcho(String text)
    {
        return echoService.echo(text);
    }
}
