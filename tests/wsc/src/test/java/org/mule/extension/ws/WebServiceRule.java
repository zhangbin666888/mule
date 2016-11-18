/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws;

import org.mule.extension.ws.consumer.SimpleService;
import org.mule.extension.ws.internal.introspection.WsdlIntrospecter;
import org.mule.metadata.xml.XmlTypeLoader;

import javax.xml.ws.Endpoint;

import org.junit.rules.ExternalResource;

public class WebServiceRule extends ExternalResource {

  private final String address;
  private Endpoint server;
  private XmlTypeLoader typeLoader;
  private WsdlIntrospecter introspecter;

  public WebServiceRule(String port) {
    this.address = "http://localhost:" + port + "/testService";
  }

  @Override
  protected void before() throws Throwable {
    super.before();
    server = Endpoint.publish(address, new SimpleService());
    introspecter = new WsdlIntrospecter(address + "?wsdl", "TestService", "TestPort");
    typeLoader = new XmlTypeLoader(introspecter.getSchemas());
  }

  @Override
  protected void after() {
    server.stop();
    super.after();
  }

  public XmlTypeLoader getTypeLoader() {
    return typeLoader;
  }

  public WsdlIntrospecter getIntrospecter() {
    return introspecter;
  }

  //public static void main(String[] args) {
  //  Endpoint.publish("http://localhost:90190/service", new SimpleService());
  //}
}
