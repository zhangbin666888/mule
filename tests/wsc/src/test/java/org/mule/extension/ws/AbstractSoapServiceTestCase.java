/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws;

import org.mule.functional.junit4.MuleArtifactFunctionalTestCase;
import org.mule.runtime.api.message.Message;
import org.mule.tck.junit4.rule.DynamicPort;
import org.mule.test.runner.ArtifactClassLoaderRunnerConfig;

import org.junit.ClassRule;

@ArtifactClassLoaderRunnerConfig(plugins = {
    "org.mule.modules:mule-module-wsc",
    "org.mule.modules:mule-module-sockets",
    "org.mule.modules:mule-module-http-ext"
})
public abstract class AbstractSoapServiceTestCase extends MuleArtifactFunctionalTestCase {

  @ClassRule
  public static DynamicPort servicePort = new DynamicPort("servicePort");

  @ClassRule
  public static WebServiceRule service = new WebServiceRule(servicePort.getValue());

  protected Message runFlowWithRequest(String flowName, String requestXmlResourceName) throws Exception {
    return flowRunner(flowName)
        .withPayload(WscTestUtils.getRequestResource(requestXmlResourceName))
        .withVariable(WscTestUtils.HEADER_IN, WscTestUtils.getRequestResource(WscTestUtils.HEADER_IN))
        .withVariable(WscTestUtils.HEADER_INOUT, WscTestUtils.getRequestResource(WscTestUtils.HEADER_INOUT))
        .run()
        .getMessage();
  }
}
