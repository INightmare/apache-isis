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

package org.apache.isis.core.metamodel.runtimecontext;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.core.commons.authentication.AuthenticationSession;
import org.apache.isis.core.commons.authentication.AuthenticationSessionProvider;
import org.apache.isis.core.commons.components.ApplicationScopedComponent;
import org.apache.isis.core.commons.components.Injectable;
import org.apache.isis.core.metamodel.adapter.DomainObjectServices;
import org.apache.isis.core.metamodel.adapter.LocalizationProvider;
import org.apache.isis.core.metamodel.adapter.ObjectDirtier;
import org.apache.isis.core.metamodel.adapter.ObjectPersistor;
import org.apache.isis.core.metamodel.adapter.QuerySubmitter;
import org.apache.isis.core.metamodel.adapter.ServicesProvider;
import org.apache.isis.core.metamodel.adapter.map.AdapterMap;
import org.apache.isis.core.metamodel.services.ServicesInjector;
import org.apache.isis.core.metamodel.spec.ObjectInstantiator;
import org.apache.isis.core.metamodel.spec.SpecificationLookup;

/**
 * Decouples the metamodel from a runtime.
 * 
 */
public interface RuntimeContext extends Injectable, ApplicationScopedComponent {

    /**
     * A mechanism for returning the <tt>current</tt>
     * {@link AuthenticationSession}.
     * 
     * <p>
     * Note that the scope of {@link RuntimeContext} is global, whereas
     * {@link AuthenticationSession} may change over time.
     */
    public AuthenticationSessionProvider getAuthenticationSessionProvider();

    public QuerySubmitter getQuerySubmitter();

    public AdapterMap getAdapterMap();

    public ObjectInstantiator getObjectInstantiator();

    public SpecificationLookup getSpecificationLookup();

    public ServicesProvider getServicesProvider();

    /**
     * aka the ServicesInjector...
     */
    public DependencyInjector getDependencyInjector();

    public ObjectDirtier getObjectDirtier();

    public ObjectPersistor getObjectPersistor();

    public DomainObjectServices getDomainObjectServices();

    public LocalizationProvider getLocalizationProvider();


    // ///////////////////////////////////////////
    // container
    // ///////////////////////////////////////////

    public void setContainer(DomainObjectContainer container);



}
