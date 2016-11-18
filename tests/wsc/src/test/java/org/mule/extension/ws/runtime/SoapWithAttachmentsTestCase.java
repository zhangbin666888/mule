/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.ws.runtime;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.mule.extension.ws.WscTestUtils;
import org.mule.runtime.api.message.Message;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Stories;

@Features("Web Service Consumer")
@Stories("Attachments")
public class SoapWithAttachmentsTestCase extends AttachmentsTestCase {

  public SoapWithAttachmentsTestCase() {
    super(false);
  }

  @Override
  protected void assertDownloadedAttachment(Message attachmentPart) throws XMLStreamException, IOException {
    String expectedAttachmentContent = WscTestUtils.resourceAsString(WscTestUtils.SIMPLE_ATTACHMENT);
    assertThat(attachmentPart.getPayload().getValue(), is(expectedAttachmentContent));
  }
}
