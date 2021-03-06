/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.core.exception;

import static org.mule.runtime.core.exception.Errors.CORE_NAMESPACE_NAME;
import static org.mule.runtime.core.exception.Errors.Identifiers.EXPRESSION_ERROR_IDENTIFIER;
import static org.mule.runtime.core.exception.Errors.Identifiers.TRANSFORMATION_ERROR_IDENTIFIER;

import org.junit.Before;
import org.mule.runtime.api.message.ErrorType;
import org.mule.runtime.api.dsl.config.ComponentIdentifier;
import org.mule.tck.junit4.AbstractMuleContextTestCase;

public abstract class AbstractErrorTypeMatcherTestCase extends AbstractMuleContextTestCase {

  protected ErrorType anyErrorType;
  protected ErrorType transformationErrorType;
  protected ErrorType expressionErrorType;

  @Before
  public void setUp() {
    ErrorTypeRepository errorTypeRepository = muleContext.getErrorTypeRepository();
    anyErrorType = errorTypeRepository.getAnyErrorType();
    ComponentIdentifier transformationIdentifier =
        new ComponentIdentifier.Builder().withName(TRANSFORMATION_ERROR_IDENTIFIER).withNamespace(CORE_NAMESPACE_NAME).build();
    transformationErrorType = errorTypeRepository.lookupErrorType(transformationIdentifier).get();
    ComponentIdentifier expressionIdentifier =
        new ComponentIdentifier.Builder().withName(EXPRESSION_ERROR_IDENTIFIER).withNamespace(CORE_NAMESPACE_NAME).build();
    expressionErrorType = errorTypeRepository.lookupErrorType(expressionIdentifier).get();
  }

}
