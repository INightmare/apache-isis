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

package org.apache.isis.runtimes.dflt.runtime.system.session;

import static org.apache.isis.core.commons.ensure.Ensure.ensureThatArg;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;

import java.util.List;

import org.apache.isis.core.commons.authentication.AuthenticationSession;
import org.apache.isis.core.commons.components.ApplicationScopedComponent;
import org.apache.isis.core.commons.config.IsisConfiguration;
import org.apache.isis.core.commons.lang.JavaClassUtils;
import org.apache.isis.core.metamodel.spec.SpecificationLoaderSpi;
import org.apache.isis.core.runtime.authentication.AuthenticationManager;
import org.apache.isis.core.runtime.authorization.AuthorizationManager;
import org.apache.isis.core.runtime.imageloader.TemplateImageLoader;
import org.apache.isis.core.runtime.userprofile.UserProfile;
import org.apache.isis.core.runtime.userprofile.UserProfileLoader;
import org.apache.isis.runtimes.dflt.runtime.installerregistry.InstallerLookup;
import org.apache.isis.runtimes.dflt.runtime.system.DeploymentType;
import org.apache.isis.runtimes.dflt.runtime.system.persistence.PersistenceSession;
import org.apache.isis.runtimes.dflt.runtime.system.persistence.PersistenceSessionFactory;

/**
 * Creates an implementation of
 * {@link IsisSessionFactory#openSession(AuthenticationSession)} to create an
 * {@link IsisSession}, but delegates to subclasses to actually obtain the
 * components that make up that {@link IsisSession}.
 * 
 * <p>
 * The idea is that one subclass can use the {@link InstallerLookup} design to
 * lookup installers for components (and hence create the components
 * themselves), whereas another subclass might simply use Spring (or another DI
 * container) to inject in the components according to some Spring-configured
 * application context.
 */
public abstract class IsisSessionFactoryAbstract implements IsisSessionFactory {

    private final DeploymentType deploymentType;
    private final IsisConfiguration configuration;
    private final TemplateImageLoader templateImageLoader;
    private final SpecificationLoaderSpi specificationLoaderSpi;
    private final AuthenticationManager authenticationManager;
    private final AuthorizationManager authorizationManager;
    private final PersistenceSessionFactory persistenceSessionFactory;
    private final UserProfileLoader userProfileLoader;
    private final List<Object> serviceList;

    public IsisSessionFactoryAbstract(final DeploymentType deploymentType, final IsisConfiguration configuration, final SpecificationLoaderSpi specificationLoader, final TemplateImageLoader templateImageLoader, final AuthenticationManager authenticationManager,
            final AuthorizationManager authorizationManager, final UserProfileLoader userProfileLoader, final PersistenceSessionFactory persistenceSessionFactory, final List<Object> serviceList) {

        ensureThatArg(deploymentType, is(not(nullValue())));
        ensureThatArg(configuration, is(not(nullValue())));
        ensureThatArg(specificationLoader, is(not(nullValue())));
        ensureThatArg(templateImageLoader, is(not(nullValue())));
        ensureThatArg(authenticationManager, is(not(nullValue())));
        ensureThatArg(authorizationManager, is(not(nullValue())));
        ensureThatArg(userProfileLoader, is(not(nullValue())));
        ensureThatArg(persistenceSessionFactory, is(not(nullValue())));
        ensureThatArg(serviceList, is(not(nullValue())));

        this.deploymentType = deploymentType;
        this.configuration = configuration;
        this.templateImageLoader = templateImageLoader;
        this.specificationLoaderSpi = specificationLoader;
        this.authenticationManager = authenticationManager;
        this.authorizationManager = authorizationManager;
        this.userProfileLoader = userProfileLoader;
        this.persistenceSessionFactory = persistenceSessionFactory;
        this.serviceList = serviceList;
    }

    // ///////////////////////////////////////////
    // init, shutdown
    // ///////////////////////////////////////////

    /**
     * Wires components as necessary, and then
     * {@link ApplicationScopedComponent#init() init}ializes all.
     */
    @Override
    public void init() {
        templateImageLoader.init();

        specificationLoaderSpi.setServiceClasses(JavaClassUtils.toClasses(serviceList));

        specificationLoaderSpi.init();

        // must come after init of spec loader.
        specificationLoaderSpi.injectInto(persistenceSessionFactory);
        persistenceSessionFactory.setServices(serviceList);
        userProfileLoader.setServices(serviceList);

        authenticationManager.init();
        authorizationManager.init();
        persistenceSessionFactory.init();
    }

    @Override
    public void shutdown() {
        persistenceSessionFactory.shutdown();
        authenticationManager.shutdown();
        specificationLoaderSpi.shutdown();
        templateImageLoader.shutdown();
        userProfileLoader.shutdown();
    }

    @Override
    public IsisSession openSession(final AuthenticationSession authenticationSession) {
        final PersistenceSession persistenceSession = persistenceSessionFactory.createPersistenceSession();
        ensureThatArg(persistenceSession, is(not(nullValue())));

        final UserProfile userProfile = userProfileLoader.getProfile(authenticationSession);
        ensureThatArg(userProfile, is(not(nullValue())));

        // inject into persistenceSession any/all application-scoped components
        // that it requires
        getSpecificationLoader().injectInto(persistenceSession);

        final IsisSessionDefault isisSessionDefault = new IsisSessionDefault(this, authenticationSession, persistenceSession, userProfile);

        return isisSessionDefault;
    }

    @Override
    public IsisConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public DeploymentType getDeploymentType() {
        return deploymentType;
    }

    @Override
    public SpecificationLoaderSpi getSpecificationLoader() {
        return specificationLoaderSpi;
    }

    @Override
    public TemplateImageLoader getTemplateImageLoader() {
        return templateImageLoader;
    }

    @Override
    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    @Override
    public AuthorizationManager getAuthorizationManager() {
        return authorizationManager;
    }

    @Override
    public PersistenceSessionFactory getPersistenceSessionFactory() {
        return persistenceSessionFactory;
    }

    @Override
    public UserProfileLoader getUserProfileLoader() {
        return userProfileLoader;
    }

    @Override
    public List<Object> getServices() {
        return serviceList;
    }
}
