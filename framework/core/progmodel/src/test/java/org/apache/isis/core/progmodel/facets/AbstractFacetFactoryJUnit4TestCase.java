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

package org.apache.isis.core.progmodel.facets;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.metamodel.facetapi.FacetHolderImpl;
import org.apache.isis.core.metamodel.facetapi.FeatureType;
import org.apache.isis.core.metamodel.facetapi.MethodRemover;
import org.apache.isis.core.metamodel.facets.FacetedMethod;
import org.apache.isis.core.metamodel.facets.FacetedMethodParameter;
import org.apache.isis.core.metamodel.spec.ObjectSpecification;
import org.apache.isis.core.metamodel.spec.feature.OneToManyAssociation;
import org.apache.isis.core.metamodel.spec.feature.OneToOneActionParameter;
import org.apache.isis.core.metamodel.spec.feature.OneToOneAssociation;
import org.apache.isis.core.metamodel.specloader.ObjectReflector;
import org.apache.isis.core.progmodel.facets.AbstractFacetFactoryTest.Customer;
import org.apache.isis.core.testsupport.jmock.JUnitRuleMockery2;
import org.apache.isis.core.testsupport.jmock.JUnitRuleMockery2.Mode;

public abstract class AbstractFacetFactoryJUnit4TestCase {

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_ONLY);

    @Mock
    protected ObjectReflector reflector;
    @Mock
    protected MethodRemover methodRemover;
    @Mock
    protected FacetHolder facetHolder;

    protected FacetHolder facetHolderImpl;

    @Mock
    protected ObjectSpecification objSpec;
    @Mock
    protected OneToOneAssociation oneToOneAssociation;
    @Mock
    protected OneToManyAssociation oneToManyAssociation;
    @Mock
    protected OneToOneActionParameter actionParameter;

    protected FacetedMethod facetedMethod;
    protected FacetedMethodParameter facetedMethodParameter;

    @Before
    public void setUpFacetedMethodAndParameter() throws Exception {
        facetHolderImpl = new FacetHolderImpl();
        facetedMethod = FacetedMethod.createProperty(Customer.class, "firstName");
        facetedMethodParameter = new FacetedMethodParameter(String.class);
    }
    
    @After
    public void tearDown() throws Exception {
        facetHolderImpl = null;
        facetedMethod = null;
        facetedMethodParameter = null;
    }
    
    protected boolean contains(final Class<?>[] types, final Class<?> type) {
        return Utils.contains(types, type);
    }

    protected boolean contains(final FeatureType[] featureTypes, final FeatureType featureType) {
        return Utils.contains(featureTypes, featureType);
    }

    protected static boolean contains(final List<FeatureType> featureTypes, final FeatureType featureType) {
        return Utils.contains(featureTypes, featureType);
    }

    protected Method findMethod(final Class<?> type, final String methodName, final Class<?>[] methodTypes) {
        return Utils.findMethod(type, methodName, methodTypes);
    }

    protected Method findMethod(final Class<?> type, final String methodName) {
        return Utils.findMethod(type, methodName);
    }

    protected void expectNoMethodsRemoved() {
        context.never(methodRemover);
    }

}
