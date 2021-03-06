/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.runtime.module.deployment.impl.internal.policy;

import static java.io.File.createTempFile;
import static java.io.File.separator;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mule.runtime.core.util.FileUtils.unzip;
import static org.mule.runtime.module.deployment.impl.internal.policy.FileSystemPolicyClassLoaderModelLoader.CLASSES_DIR;
import static org.mule.runtime.module.deployment.impl.internal.policy.FileSystemPolicyClassLoaderModelLoader.LIB_DIR;
import static org.mule.runtime.module.deployment.impl.internal.policy.PolicyTemplateDescriptorFactory.FILE_SYSTEM_MODEL_LOADER_ID;
import static org.mule.runtime.module.deployment.impl.internal.policy.PolicyTemplateDescriptorFactory.MISSING_POLICY_DESCRIPTOR_ERROR;
import static org.mule.runtime.module.deployment.impl.internal.policy.PolicyTemplateDescriptorFactory.PROPERTIES_BUNDLE_DESCRIPTOR_LOADER_ID;
import static org.mule.runtime.module.deployment.impl.internal.policy.PropertiesBundleDescriptorLoader.ARTIFACT_ID;
import static org.mule.runtime.module.deployment.impl.internal.policy.PropertiesBundleDescriptorLoader.CLASSIFIER;
import static org.mule.runtime.module.deployment.impl.internal.policy.PropertiesBundleDescriptorLoader.GROUP_ID;
import static org.mule.runtime.module.deployment.impl.internal.policy.PropertiesBundleDescriptorLoader.TYPE;
import static org.mule.runtime.module.deployment.impl.internal.policy.PropertiesBundleDescriptorLoader.VERSION;
import org.mule.runtime.api.deployment.meta.MuleArtifactLoaderDescriptor;
import org.mule.runtime.api.deployment.meta.MulePolicyModel.MulePolicyModelBuilder;
import org.mule.runtime.deployment.model.api.plugin.ArtifactPluginRepository;
import org.mule.runtime.deployment.model.api.policy.PolicyTemplateDescriptor;
import org.mule.runtime.module.artifact.descriptor.ArtifactDescriptorCreateException;
import org.mule.runtime.module.deployment.impl.internal.builder.PolicyFileBuilder;
import org.mule.tck.junit4.AbstractMuleTestCase;
import org.mule.tck.size.SmallTest;
import org.mule.tck.util.CompilerUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

@SmallTest
public class PolicyTemplateDescriptorFactoryTestCase extends AbstractMuleTestCase {

  private static final String POLICY_NAME = "testPolicy";
  private static final String JAR_FILE_NAME = "test.jar";
  private static final String POLICY_VERSION = "1.0";
  private static final String POLICY_GROUP_ID = "org.mule.test";
  private static final String POLICY_CLASSIFIER = "mule-policy";
  private static final String POLICY_ARTIFACT_TYPE = "zip";

  private static final File echoTestJarFile =
      new CompilerUtils.JarCompiler().compiling(getResourceFile("/org/foo/EchoTest.java"))
          .compile(JAR_FILE_NAME);


  private static File getResourceFile(String resource) {
    return new File(PolicyTemplateDescriptorFactoryTestCase.class.getResource(resource).getFile());
  }

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  private ArtifactPluginRepository applicationPluginRepository;

  @Before
  public void setUp() throws Exception {
    applicationPluginRepository = mock(ArtifactPluginRepository.class);
    when(applicationPluginRepository.getContainerArtifactPluginDescriptors()).thenReturn(emptyList());
  }

  @Test
  public void verifiesThatPolicyDescriptorIsPresent() throws Exception {
    PolicyFileBuilder policyFileBuilder = new PolicyFileBuilder(POLICY_NAME).usingLibrary(echoTestJarFile.getAbsolutePath());
    File tempFolder = createTempFolder();
    unzip(policyFileBuilder.getArtifactFile(), tempFolder);

    PolicyTemplateDescriptorFactory descriptorFactory = new PolicyTemplateDescriptorFactory();

    expectedException.expect(ArtifactDescriptorCreateException.class);
    expectedException.expectMessage(MISSING_POLICY_DESCRIPTOR_ERROR);
    descriptorFactory.create(tempFolder);
  }

  @Test
  public void readsRuntimeLibs() throws Exception {
    MulePolicyModelBuilder mulePolicyModelBuilder = new MulePolicyModelBuilder().setName(POLICY_NAME).setMinMuleVersion("4.0.0")
        .withBundleDescriptorLoader(createPolicyBundleDescriptorLoader());
    mulePolicyModelBuilder.withClassLoaderModelDescriber().setId(FILE_SYSTEM_MODEL_LOADER_ID);

    PolicyFileBuilder policyFileBuilder = new PolicyFileBuilder(POLICY_NAME).usingLibrary(echoTestJarFile.getAbsolutePath())
        .describedBy(mulePolicyModelBuilder.build());
    File tempFolder = createTempFolder();
    unzip(policyFileBuilder.getArtifactFile(), tempFolder);

    PolicyTemplateDescriptorFactory descriptorFactory = new PolicyTemplateDescriptorFactory();
    PolicyTemplateDescriptor desc = descriptorFactory.create(tempFolder);

    assertThat(desc.getClassLoaderModel().getUrls().length, equalTo(2));
    assertThat(desc.getClassLoaderModel().getUrls()[0].getFile(), equalTo(new File(tempFolder, CLASSES_DIR).toString()));
    assertThat(desc.getClassLoaderModel().getUrls()[1].getFile(),
               equalTo(new File(tempFolder, LIB_DIR + separator + JAR_FILE_NAME).toString()));
  }

  @Test
  public void assignsBundleDescriptor() throws Exception {
    MulePolicyModelBuilder mulePolicyModelBuilder = new MulePolicyModelBuilder().setName(POLICY_NAME).setMinMuleVersion("4.0.0")
        .withBundleDescriptorLoader(createPolicyBundleDescriptorLoader());


    PolicyFileBuilder policyFileBuilder = new PolicyFileBuilder(POLICY_NAME).usingLibrary(echoTestJarFile.getAbsolutePath())
        .describedBy(mulePolicyModelBuilder.build());
    File tempFolder = createTempFolder();
    unzip(policyFileBuilder.getArtifactFile(), tempFolder);

    PolicyTemplateDescriptorFactory descriptorFactory = new PolicyTemplateDescriptorFactory();
    PolicyTemplateDescriptor desc = descriptorFactory.create(tempFolder);

    assertThat(desc.getBundleDescriptor().getArtifactId(), equalTo(POLICY_NAME));
    assertThat(desc.getBundleDescriptor().getGroupId(), equalTo(POLICY_GROUP_ID));
    assertThat(desc.getBundleDescriptor().getClassifier().get(), equalTo(POLICY_CLASSIFIER));
    assertThat(desc.getBundleDescriptor().getType(), equalTo(POLICY_ARTIFACT_TYPE));
    assertThat(desc.getBundleDescriptor().getVersion(), equalTo(POLICY_VERSION));
  }

  private MuleArtifactLoaderDescriptor createPolicyBundleDescriptorLoader() {
    Map<String, Object> attributes = new HashMap();
    attributes.put(VERSION, POLICY_VERSION);
    attributes.put(GROUP_ID, POLICY_GROUP_ID);
    attributes.put(ARTIFACT_ID, POLICY_NAME);
    attributes.put(CLASSIFIER, POLICY_CLASSIFIER);
    attributes.put(TYPE, POLICY_ARTIFACT_TYPE);
    return new MuleArtifactLoaderDescriptor(PROPERTIES_BUNDLE_DESCRIPTOR_LOADER_ID, attributes);
  }

  private File createTempFolder() throws IOException {
    File tempFolder = createTempFile("tempPolicy", null);
    assertThat(tempFolder.delete(), is(true));
    assertThat(tempFolder.mkdir(), is(true));
    return tempFolder;
  }
}
