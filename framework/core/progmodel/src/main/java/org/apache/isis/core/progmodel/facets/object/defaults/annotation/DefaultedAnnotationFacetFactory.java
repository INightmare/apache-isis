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

package org.apache.isis.core.progmodel.facets.object.defaults.annotation;

import org.apache.isis.applib.annotation.Defaulted;
import org.apache.isis.core.commons.config.IsisConfiguration;
import org.apache.isis.core.commons.config.IsisConfigurationAware;
import org.apache.isis.core.commons.lang.StringUtils;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.metamodel.facetapi.FacetUtil;
import org.apache.isis.core.metamodel.facetapi.FeatureType;
import org.apache.isis.core.metamodel.facets.AnnotationBasedFacetFactoryAbstract;
import org.apache.isis.core.metamodel.runtimecontext.DependencyInjector;
import org.apache.isis.core.metamodel.runtimecontext.DependencyInjectorAware;
import org.apache.isis.core.progmodel.facets.object.defaults.DefaultedFacetAbstract;
import org.apache.isis.core.progmodel.facets.object.defaults.DefaultsProviderUtil;

public class DefaultedAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract implements IsisConfigurationAware, DependencyInjectorAware {

    private IsisConfiguration configuration;
    private DependencyInjector dependencyInjector;

    public DefaultedAnnotationFacetFactory() {
        super(FeatureType.OBJECTS_ONLY);
    }

    @Override
    public void process(final ProcessClassContext processClassContext) {
        FacetUtil.addFacet(create(processClassContext.getCls(), processClassContext.getFacetHolder()));
    }

    private DefaultedFacetAbstract create(final Class<?> cls, final FacetHolder holder) {
        final Defaulted annotation = getAnnotation(cls, Defaulted.class);

        // create from annotation, if present
        if (annotation != null) {
            final DefaultedFacetAbstract facet = new DefaultedFacetAnnotation(cls, getIsisConfiguration(), holder, getDependencyInjector());
            if (facet.isValid()) {
                return facet;
            }
        }

        // otherwise, try to create from configuration, if present
        final String providerName = DefaultsProviderUtil.defaultsProviderNameFromConfiguration(cls, getIsisConfiguration());
        if (!StringUtils.isNullOrEmpty(providerName)) {
            final DefaultedFacetFromConfiguration facet = new DefaultedFacetFromConfiguration(providerName, holder, getDependencyInjector());
            if (facet.isValid()) {
                return facet;
            }
        }

        return null;
    }

    // ////////////////////////////////////////////////////////////////////
    // Injected
    // ////////////////////////////////////////////////////////////////////

    public IsisConfiguration getIsisConfiguration() {
        return configuration;
    }

    @Override
    public void setConfiguration(final IsisConfiguration configuration) {
        this.configuration = configuration;
    }

    private DependencyInjector getDependencyInjector() {
        return dependencyInjector;
    }

    @Override
    public void setDependencyInjector(final DependencyInjector dependencyInjector) {
        this.dependencyInjector = dependencyInjector;
    }

}
