/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.isis.runtimes.dflt.runtime.context;

import java.util.Collections;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.apache.isis.core.commons.authentication.AuthenticationSession;
import org.apache.isis.core.commons.config.IsisConfiguration;
import org.apache.isis.core.commons.config.IsisConfigurationDefault;
import org.apache.isis.core.metamodel.spec.SpecificationLoaderSpi;
import org.apache.isis.core.metamodel.spec.SpecificationLoaderSpiAware;
import org.apache.isis.core.runtime.authentication.AuthenticationManager;
import org.apache.isis.core.runtime.authentication.standard.SimpleSession;
import org.apache.isis.core.runtime.authorization.AuthorizationManager;
import org.apache.isis.core.runtime.imageloader.TemplateImageLoader;
import org.apache.isis.core.runtime.userprofile.UserProfileLoader;
import org.apache.isis.core.testsupport.jmock.IsisActions;
import org.apache.isis.core.testsupport.jmock.JUnitRuleMockery2;
import org.apache.isis.core.testsupport.jmock.JUnitRuleMockery2.Mode;
import org.apache.isis.runtimes.dflt.runtime.system.DeploymentType;
import org.apache.isis.runtimes.dflt.runtime.system.context.IsisContext;
import org.apache.isis.runtimes.dflt.runtime.system.context.IsisContextStatic;
import org.apache.isis.runtimes.dflt.runtime.system.persistence.PersistenceSession;
import org.apache.isis.runtimes.dflt.runtime.system.persistence.PersistenceSessionFactory;
import org.apache.isis.runtimes.dflt.runtime.system.session.IsisSessionFactory;
import org.apache.isis.runtimes.dflt.runtime.system.session.IsisSessionFactoryDefault;

public class IsisContextTest {

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);
    

    private IsisConfiguration configuration;
    
    @Mock
    private PersistenceSession mockPersistenceSession;
    
    @Mock
    private SpecificationLoaderSpi mockSpecificationLoader;

    @Mock
    protected TemplateImageLoader mockTemplateImageLoader;
    @Mock
    protected PersistenceSessionFactory mockPersistenceSessionFactory;
    @Mock
    private UserProfileLoader mockUserProfileLoader;
    @Mock
    protected AuthenticationManager mockAuthenticationManager;
    @Mock
    protected AuthorizationManager mockAuthorizationManager;

    private List<Object> servicesList;


    private AuthenticationSession authSession;


    private IsisSessionFactory sessionFactory;

    @Before
    public void setUp() throws Exception {
        IsisContext.testReset();

        servicesList = Collections.emptyList();

        configuration = new IsisConfigurationDefault();
        
        context.checking(new Expectations() {
            {
                allowing(mockPersistenceSessionFactory).createPersistenceSession();
                will(returnValue(mockPersistenceSession));
                
                ignoring(mockPersistenceSession);
                ignoring(mockSpecificationLoader);
                ignoring(mockPersistenceSessionFactory);
                ignoring(mockUserProfileLoader);
                ignoring(mockAuthenticationManager);
                ignoring(mockAuthorizationManager);
                ignoring(mockTemplateImageLoader);
            }
        });

        sessionFactory = new IsisSessionFactoryDefault(DeploymentType.EXPLORATION, configuration, mockTemplateImageLoader, mockSpecificationLoader, mockAuthenticationManager, mockAuthorizationManager, mockUserProfileLoader, mockPersistenceSessionFactory, servicesList);
        authSession = new SimpleSession("tester", Collections.<String>emptyList());
        
        IsisContext.setConfiguration(configuration);
    }
    
    @After
    public void tearDown() throws Exception {
        if(IsisContext.inSession()) {
            IsisContext.closeSession();
        }
    }
    
    @Test
    public void getConfiguration() {
        IsisContextStatic.createRelaxedInstance(sessionFactory);
        Assert.assertEquals(configuration, IsisContext.getConfiguration());
    }

    @Test
    public void openSession_getSpecificationLoader() {
        IsisContextStatic.createRelaxedInstance(sessionFactory);
        IsisContext.openSession(authSession);

        Assert.assertEquals(mockSpecificationLoader, IsisContext.getSpecificationLoader());
    }

    @Test
    public void openSession_getAuthenticationLoader() {
        IsisContextStatic.createRelaxedInstance(sessionFactory);
        IsisContext.openSession(authSession);

        Assert.assertEquals(authSession, IsisContext.getAuthenticationSession());
    }
    
    @Test
    public void openSession_getPersistenceSession() {
        IsisContextStatic.createRelaxedInstance(sessionFactory);
        IsisContext.openSession(authSession);

        Assert.assertSame(mockPersistenceSession, IsisContext.getPersistenceSession());
    }


}
