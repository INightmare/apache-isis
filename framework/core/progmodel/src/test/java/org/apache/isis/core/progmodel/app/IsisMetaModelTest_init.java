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

package org.apache.isis.core.progmodel.app;

import java.util.TreeSet;

import com.google.common.collect.Lists;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.auto.Mock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.core.commons.config.IsisConfiguration;
import org.apache.isis.core.metamodel.facetdecorator.FacetDecorator;
import org.apache.isis.core.metamodel.progmodel.ProgrammingModel;
import org.apache.isis.core.metamodel.runtimecontext.RuntimeContext;
import org.apache.isis.core.metamodel.specloader.classsubstitutor.ClassSubstitutor;
import org.apache.isis.core.metamodel.specloader.collectiontyperegistry.CollectionTypeRegistry;
import org.apache.isis.core.progmodel.app.IsisMetaModel;
import org.apache.isis.core.testsupport.jmock.IsisActions;
import org.apache.isis.core.testsupport.jmock.JUnitRuleMockery2;
import org.apache.isis.core.testsupport.jmock.JUnitRuleMockery2.Mode;

public class IsisMetaModelTest_init {

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

    @Mock
    private IsisConfiguration mockConfiguration;
    @Mock
    private ProgrammingModel mockProgrammingModelFacets;
    @Mock
    private FacetDecorator mockFacetDecorator;
    @Mock
    private ClassSubstitutor mockClassSubstitutor;
    @Mock
    private CollectionTypeRegistry mockCollectionTypeRegistry;
    @Mock
    private RuntimeContext mockRuntimeContext;

    private IsisMetaModel metaModel;

    @Before
    public void setUp() {
        expectingMetaModelToBeInitialized();
        metaModel = new IsisMetaModel(mockRuntimeContext, mockProgrammingModelFacets);
    }

    private void expectingMetaModelToBeInitialized() {
        final Sequence initSequence = context.sequence("init");
        context.checking(new Expectations() {
            {
                allowing(mockRuntimeContext).injectInto(with(any(Object.class)));
                will(IsisActions.injectInto());
                
                one(mockRuntimeContext).setContainer(with(any(DomainObjectContainer.class)));
                inSequence(initSequence);
                
                one(mockProgrammingModelFacets).init();
                inSequence(initSequence);
                
                one(mockProgrammingModelFacets).getList();
                inSequence(initSequence);
                will(returnValue(Lists.newArrayList()));
                
                one(mockRuntimeContext).init();
                inSequence(initSequence);
            }
        });
    }

    @Test
    public void shouldSucceedWithoutThrowingAnyExceptions() {
        metaModel.init();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotBeAbleToChangeConfiguration() {
        metaModel.init();
        metaModel.setConfiguration(mockConfiguration);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotBeAbleToChangeProgrammingModelFacets() {
        metaModel.init();
        metaModel.setProgrammingModelFacets(mockProgrammingModelFacets);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotBeAbleToChangeCollectionTypeRegistry() {
        metaModel.init();
        metaModel.setCollectionTypeRegistry(mockCollectionTypeRegistry);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotBeAbleToChangeClassSubstitutor() {
        metaModel.init();
        metaModel.setClassSubstitutor(mockClassSubstitutor);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotBeAbleToChangeFacetDecorators() {
        metaModel.init();
        metaModel.setFacetDecorators(new TreeSet<FacetDecorator>());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldNotBeAbleToAddToFacetDecorators() {
        metaModel.init();
        metaModel.getFacetDecorators().add(mockFacetDecorator);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotBeAbleToInitializeAgain() {
        metaModel.init();
        //
        metaModel.init();
    }

    @Test
    public void shouldPrime() {
        metaModel.init();

    }

}
