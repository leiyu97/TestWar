/* Copyright (c) 2008 Commonwealth of Australia.  All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * the Commonwealth of Australia. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with
 * the Commonwealth of Australia.
 */

package org.larinia.ejb;

//import org.jboss.ejb3.annotation.RunAsPrincipal;
//import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.security.SecurityDomain;

import javax.annotation.security.RolesAllowed;
import javax.annotation.security.RunAs;
import javax.ejb.Stateless;

@Stateless
@RolesAllowed("Grp_Genesis_Users")
//@SecurityDomain("ldap")
public class TestEchoSession implements TestEchoService
      /*  extends
        SecureBaseSession */
{
    public String echo(String text)
    {
        String str = "TestEchoSession says: " + text;
        return str;
    }
}
