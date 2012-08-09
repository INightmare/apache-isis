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

package org.apache.isis.runtimes.dflt.objectstores.dflt.internal;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import com.google.common.collect.Lists;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.apache.isis.core.commons.authentication.AuthenticationSession;
import org.apache.isis.core.commons.matchers.IsisMatchers;
import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.spec.ObjectSpecification;
import org.apache.isis.core.testsupport.jmock.IsisActions;
import org.apache.isis.core.testsupport.jmock.JUnitRuleMockery2;
import org.apache.isis.core.testsupport.jmock.JUnitRuleMockery2.Mode;
import org.apache.isis.runtimes.dflt.runtime.persistence.query.PersistenceQueryBuiltIn;
import org.apache.isis.runtimes.dflt.runtime.system.persistence.AdapterManagerSpi;

public class ObjectStoreInstances_findInstancesAndAdd {

    private ObjectStoreInstances instances;

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

    @Mock
    private ObjectSpecification mockSpec;
    @Mock
    private PersistenceQueryBuiltIn mockPersistenceQueryBuiltIn;
    @Mock
    private AuthenticationSession mockAuthSession;
    @Mock
    private AdapterManagerSpi mockAdapterManager;

    @Mock
    private ObjectAdapter mockAdapter1;
    @Mock
    private ObjectAdapter mockAdapter2;

    @Before
    public void setUp() throws Exception {
        instances = new ObjectStoreInstances(mockSpec) {
            @Override
            protected AuthenticationSession getAuthenticationSession() {
                return mockAuthSession;
            }
            @Override
            protected AdapterManagerSpi getAdapterManager() {
                return mockAdapterManager;
            }
        };
        context.ignoring(mockAuthSession);
    }

    @Test
    public void findInstancesAndAdd_whenEmpty() throws Exception {
        context.never(mockPersistenceQueryBuiltIn);
        final List<ObjectAdapter> foundInstances = Lists.newArrayList();
        instances.findInstancesAndAdd(mockPersistenceQueryBuiltIn, foundInstances);
    }

    @Test
    public void findInstancesAndAdd_whenNotEmpty() throws Exception {
        context.ignoring(mockAdapter1, mockAdapter2);
        context.checking(new Expectations() {
            {
                one(mockPersistenceQueryBuiltIn).matches(mockAdapter1);
                will(returnValue(false));

                one(mockPersistenceQueryBuiltIn).matches(mockAdapter2);
                will(returnValue(true));
                
                allowing(mockAdapterManager).getAdapterFor(with(any(Object.class)));
                will(IsisActions.returnEach(mockAdapter1, mockAdapter2));
            }
        });
        
        instances.save(mockAdapter1);
        instances.save(mockAdapter2);
        
        final List<ObjectAdapter> foundInstances = Lists.newArrayList();
        instances.findInstancesAndAdd(mockPersistenceQueryBuiltIn, foundInstances);
        
        assertThat(foundInstances.size(), is(1));
        assertThat(foundInstances, IsisMatchers.listContaining(mockAdapter2));
    }

}
