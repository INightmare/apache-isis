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

import java.util.List;

import org.apache.isis.core.commons.config.IsisConfiguration;
import org.apache.isis.core.metamodel.adapter.oid.OidMarshaller;
import org.apache.isis.core.metamodel.spec.SpecificationLoaderSpi;
import org.apache.isis.core.runtime.authentication.AuthenticationManager;
import org.apache.isis.core.runtime.authorization.AuthorizationManager;
import org.apache.isis.core.runtime.imageloader.TemplateImageLoader;
import org.apache.isis.core.runtime.userprofile.UserProfileLoader;
import org.apache.isis.runtimes.dflt.runtime.system.DeploymentType;
import org.apache.isis.runtimes.dflt.runtime.system.persistence.PersistenceSessionFactory;

/**
 * As its superclass, but provides a default for some of more basic components
 * (that is, where the core framework offers only a single implementation).
 */
public class IsisSessionFactoryDefault extends IsisSessionFactoryAbstract {

    public IsisSessionFactoryDefault(final DeploymentType deploymentType, final IsisConfiguration configuration, final TemplateImageLoader templateImageLoader, final SpecificationLoaderSpi specificationLoader, final AuthenticationManager authenticationManager,
            final AuthorizationManager authorizationManager, final UserProfileLoader userProfileLoader, final PersistenceSessionFactory persistenceSessionFactory, final List<Object> servicesList, OidMarshaller oidMarshaller) {
        super(deploymentType, configuration, specificationLoader, templateImageLoader, authenticationManager, authorizationManager, userProfileLoader, persistenceSessionFactory, servicesList, oidMarshaller);
    }

}
