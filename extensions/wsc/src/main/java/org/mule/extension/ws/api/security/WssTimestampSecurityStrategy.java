/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api.security;

import static java.lang.String.valueOf;
import static org.apache.ws.security.handler.WSHandlerConstants.TIMESTAMP;
import static org.apache.ws.security.handler.WSHandlerConstants.TTL_TIMESTAMP;
import static org.mule.extension.ws.internal.security.SecurityStrategyType.OUTGOING;
import org.mule.extension.ws.internal.security.SecurityStrategyType;
import org.mule.extension.ws.internal.security.callback.WSPasswordCallbackHandler;
import org.mule.runtime.api.tls.TlsContextFactory;
import org.mule.runtime.extension.api.annotation.param.Parameter;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Optional;

public class WssTimestampSecurityStrategy implements SecurityStrategy {

  @Parameter
  private long expires;

  @Override
  public SecurityStrategyType securityType() {
    return OUTGOING;
  }

  @Override
  public Optional<WSPasswordCallbackHandler> buildPasswordCallbackHandler() {
    return Optional.empty();
  }

  @Override
  public void initializeTlsContextFactory(TlsContextFactory tlsContextFactory) {
    // no initialization required
  }

  @Override
  public String securityAction() {
    return TIMESTAMP;
  }

  @Override
  public Map<String, Object> buildSecurityProperties() {
    return ImmutableMap.<String, Object>builder().put(TTL_TIMESTAMP, valueOf(expires)).build();
  }
}
