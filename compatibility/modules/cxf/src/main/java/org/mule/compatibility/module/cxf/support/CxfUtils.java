/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.compatibility.module.cxf.support;

import java.util.Iterator;
import java.util.List;

import org.apache.cxf.binding.soap.SoapVersion;
import org.apache.cxf.binding.soap.SoapVersionFactory;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptor;

public final class CxfUtils {

  @SuppressWarnings("unchecked")
  public static boolean removeInterceptor(List<Interceptor<? extends Message>> inInterceptors, String name) {

    for (Interceptor<?> i : inInterceptors) {
      if (i instanceof PhaseInterceptor) {
        PhaseInterceptor<Message> p = (PhaseInterceptor<Message>) i;

        if (p.getId().equals(name)) {
          inInterceptors.remove(p);
          return true;
        }
      }
    }

    return false;
  }

  public static String getBindingIdForSoapVersion(String version) {
    Iterator<SoapVersion> soapVersions = SoapVersionFactory.getInstance().getVersions();
    while (soapVersions.hasNext()) {
      SoapVersion soapVersion = soapVersions.next();
      if (Double.toString(soapVersion.getVersion()).equals(version)) {
        return soapVersion.getBindingId();
      }
    }
    throw new IllegalArgumentException("Invalid Soap version " + version);
  }

  public static String mapUnsupportedSchemas(String url) {
    // hack for CXF to work correctly with servlet and jetty urls
    if (url != null) {
      url = url.replace("servlet://", "http://");
      url = url.replace("jetty://", "http://");
      url = url.replace("jetty-ssl://", "https://");
    }
    return url;
  }

}
