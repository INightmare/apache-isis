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

package org.apache.isis.core.progmodel.facets.members.disable.staticmethod;

import java.lang.reflect.Method;

import org.apache.isis.core.commons.lang.NameUtils;
import org.apache.isis.core.metamodel.adapter.util.InvokeUtils;
import org.apache.isis.core.metamodel.exceptions.MetaModelException;
import org.apache.isis.core.metamodel.facetapi.Facet;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.metamodel.facetapi.FacetUtil;
import org.apache.isis.core.metamodel.facetapi.FeatureType;
import org.apache.isis.core.metamodel.methodutils.MethodScope;
import org.apache.isis.core.progmodel.facets.MethodFinderUtils;
import org.apache.isis.core.progmodel.facets.MethodPrefixBasedFacetFactoryAbstract;
import org.apache.isis.core.progmodel.facets.MethodPrefixConstants;

public class DisabledFacetViaProtectMethodFacetFactory extends MethodPrefixBasedFacetFactoryAbstract {

    private static final String[] PREFIXES = { MethodPrefixConstants.PROTECT_PREFIX };

    /**
     * Note that the {@link Facet}s registered are the generic ones from
     * noa-architecture (where they exist)
     */
    public DisabledFacetViaProtectMethodFacetFactory() {
        super(FeatureType.MEMBERS, PREFIXES);
    }

    // ///////////////////////////////////////////////////////
    // Actions
    // ///////////////////////////////////////////////////////

    @Override
    public void process(final ProcessMethodContext processMethodContext) {
        attachDisabledFacetIfProtectMethodIsFound(processMethodContext);
    }

    public static void attachDisabledFacetIfProtectMethodIsFound(final ProcessMethodContext processMethodContext) {

        final Class<?>[] paramTypes = new Class[] {};

        final Class<?> type = processMethodContext.getCls();
        final Method method = processMethodContext.getMethod();

        final String capitalizedName = NameUtils.javaBaseNameStripAccessorPrefixIfRequired(method.getName());

        final Method protectMethod = MethodFinderUtils.findMethodWithOrWithoutParameters(type, MethodScope.CLASS, MethodPrefixConstants.PROTECT_PREFIX + capitalizedName, boolean.class, paramTypes);
        if (protectMethod == null) {
            return;
        }

        processMethodContext.removeMethod(protectMethod);

        final Boolean protectMethodReturnValue = invokeProtectMethod(protectMethod);
        if (!protectMethodReturnValue.booleanValue()) {
            return;
        }

        final FacetHolder facetedMethod = processMethodContext.getFacetHolder();
        FacetUtil.addFacet(new DisabledFacetAlwaysEverywhere(facetedMethod));
    }

    private static Boolean invokeProtectMethod(final Method protectMethod) {
        Boolean protectMethodReturnValue = null;
        try {
            protectMethodReturnValue = (Boolean) InvokeUtils.invokeStatic(protectMethod);
        } catch (final ClassCastException ex) {
            // ignore
        }
        if (protectMethodReturnValue == null) {
            throw new MetaModelException("method " + protectMethod + "must return a boolean");
        }
        return protectMethodReturnValue;
    }

}
