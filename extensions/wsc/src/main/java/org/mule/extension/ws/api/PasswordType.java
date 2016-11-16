/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.api;

public enum PasswordType {

  TEXT("PasswordText"),

  DIGEST("PasswordDigest");

  private final String type;

  PasswordType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
