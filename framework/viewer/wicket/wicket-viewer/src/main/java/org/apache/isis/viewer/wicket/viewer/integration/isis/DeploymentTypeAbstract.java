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

package org.apache.isis.viewer.wicket.viewer.integration.isis;

import org.apache.wicket.Application;

import org.apache.isis.runtimes.dflt.runtime.system.ContextCategory;
import org.apache.isis.runtimes.dflt.runtime.system.DeploymentCategory;
import org.apache.isis.runtimes.dflt.runtime.system.DeploymentType;
import org.apache.isis.runtimes.dflt.runtime.system.Splash;

/**
 * Simple adapter for Isis' {@link DeploymentType} class, specifying that the
 * {@link IsisContextForWicket.WicketContextCategory} is used as the
 * {@link ContextCategory}.
 * 
 * <p>
 * TODO: should somehow tie this in with Wicket's own
 * {@link Application#getConfigurationType() configuration mode}.
 */
public abstract class DeploymentTypeAbstract extends DeploymentType {

    public DeploymentTypeAbstract(final String name, final DeploymentCategory category) {
        super(name, category, new IsisContextForWicket.WicketContextCategory(), null, Splash.NO_SHOW);
    }

}
