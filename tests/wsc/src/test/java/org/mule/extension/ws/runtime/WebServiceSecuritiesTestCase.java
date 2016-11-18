/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.runtime;

import static java.util.Arrays.asList;
import static org.mule.extension.ws.WscTestUtils.ECHO;
import org.mule.extension.ws.AbstractSoapServiceTestCase;
import org.mule.runtime.api.message.Message;
import org.mule.test.runner.RunnerDelegateTo;

import java.util.Collection;

import org.junit.Test;
import org.junit.runners.Parameterized;

@RunnerDelegateTo(Parameterized.class)
public class WebServiceSecuritiesTestCase extends AbstractSoapServiceTestCase {

  @Parameterized.Parameters(name = "{0}")
  public static Collection<Object[]> data() {
    return asList(new Object[][] {
        {"timestamp"},
        {"usernameToken"},
        {"decrypt"},
        {"encrypt"},
        {"verifySignature"},
        {"sign"}});
  }


  private final String security;

  public WebServiceSecuritiesTestCase(String security) {
    this.security = security;
  }

  @Override
  protected String getConfigFile() {
    return "config/security/" + security + ".xml";
  }

  @Test
  public void securedRequestReturnsExpectedResult() throws Exception {

    Message operation = runFlowWithRequest("successOperation", ECHO);
    while (true) {

    }

    //String value = (String) operation.getPayload().getValue();
    //WscTestUtils.assertSimilarXml(WscTestUtils.getResponseResource(ECHO), value);
  }
}
