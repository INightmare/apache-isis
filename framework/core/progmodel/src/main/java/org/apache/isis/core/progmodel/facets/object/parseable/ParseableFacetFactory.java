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

package org.apache.isis.core.progmodel.facets.object.parseable;

import org.apache.isis.applib.annotation.Parseable;
import org.apache.isis.core.commons.authentication.AuthenticationSessionProvider;
import org.apache.isis.core.commons.authentication.AuthenticationSessionProviderAware;
import org.apache.isis.core.commons.config.IsisConfiguration;
import org.apache.isis.core.commons.config.IsisConfigurationAware;
import org.apache.isis.core.commons.lang.StringUtils;
import org.apache.isis.core.metamodel.adapter.map.AdapterManager;
import org.apache.isis.core.metamodel.adapter.map.AdapterManagerAware;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.metamodel.facetapi.FacetUtil;
import org.apache.isis.core.metamodel.facetapi.FeatureType;
import org.apache.isis.core.metamodel.facets.AnnotationBasedFacetFactoryAbstract;
import org.apache.isis.core.metamodel.runtimecontext.ServicesInjector;
import org.apache.isis.core.metamodel.runtimecontext.ServicesInjectorAware;

public class ParseableFacetFactory extends AnnotationBasedFacetFactoryAbstract implements IsisConfigurationAware, AuthenticationSessionProviderAware, AdapterManagerAware, ServicesInjectorAware {

    private IsisConfiguration configuration;

    private AuthenticationSessionProvider authenticationSessionProvider;
    private AdapterManager adapterManager;
    private ServicesInjector servicesInjector;

    public ParseableFacetFactory() {
        super(FeatureType.OBJECTS_ONLY);
    }

    @Override
    public void process(final ProcessClassContext processClassContaxt) {
        FacetUtil.addFacet(create(processClassContaxt.getCls(), processClassContaxt.getFacetHolder()));
    }

    private ParseableFacetAbstract create(final Class<?> cls, final FacetHolder holder) {
        final Parseable annotation = getAnnotation(cls, Parseable.class);

        // create from annotation, if present
        if (annotation != null) {
            final ParseableFacetAnnotation facet = new ParseableFacetAnnotation(cls, getIsisConfiguration(), holder, authenticationSessionProvider, adapterManager, servicesInjector);
            if (facet.isValid()) {
                return facet;
            }
        }

        // otherwise, try to create from configuration, if present
        final String parserName = ParserUtil.parserNameFromConfiguration(cls, getIsisConfiguration());
        if (!StringUtils.isNullOrEmpty(parserName)) {
            final ParseableFacetFromConfiguration facet = new ParseableFacetFromConfiguration(parserName, holder, authenticationSessionProvider, servicesInjector, adapterManager);
            if (facet.isValid()) {
                return facet;
            }
        }

        return null;
    }

    // ////////////////////////////////////////////////////////////////////
    // Dependencies (injected via setters since *Aware)
    // ////////////////////////////////////////////////////////////////////

    public IsisConfiguration getIsisConfiguration() {
        return configuration;
    }

    /**
     * Injected since {@link IsisConfigurationAware}.
     */
    @Override
    public void setConfiguration(final IsisConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void setAuthenticationSessionProvider(final AuthenticationSessionProvider authenticationSessionProvider) {
        this.authenticationSessionProvider = authenticationSessionProvider;
    }

    @Override
    public void setAdapterManager(final AdapterManager adapterManager) {
        this.adapterManager = adapterManager;
    }

    @Override
    public void setServicesInjector(final ServicesInjector dependencyInjector) {
        this.servicesInjector = dependencyInjector;
    }

}
