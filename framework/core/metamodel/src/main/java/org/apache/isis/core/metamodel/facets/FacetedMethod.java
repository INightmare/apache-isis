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

package org.apache.isis.core.metamodel.facets;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import org.apache.isis.applib.Identifier;
import org.apache.isis.core.commons.debug.DebugBuilder;
import org.apache.isis.core.commons.debug.Debuggable;
import org.apache.isis.core.commons.lang.StringUtils;
import org.apache.isis.core.metamodel.facetapi.FeatureType;
import org.apache.isis.core.metamodel.facetapi.IdentifiedHolder;

/**
 * non-final only so it can be mocked if need be.
 */
public class FacetedMethod extends TypedHolderDefault implements IdentifiedHolder, Debuggable {

    // //////////////////////////////////////////////////
    // Factory methods
    // //////////////////////////////////////////////////

    public static FacetedMethod createPropertyFacetedMethod(final Class<?> declaringType, final Method method) {
        return new FacetedMethod(FeatureType.PROPERTY, declaringType, method, method.getReturnType(), emptyParameterList());
    }

    /**
     * Principally for testing purposes.
     */
    public static FacetedMethod createProperty(final Class<?> owningClass, final String name) {
        try {
            final Method method = owningClass.getMethod("get" + StringUtils.pascal(name));
            return FacetedMethod.createPropertyFacetedMethod(owningClass, method);
        } catch (final SecurityException e) {
            throw new RuntimeException(e);
        } catch (final NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static FacetedMethod createCollectionFacetedMethod(final Class<?> declaringType, final Method method) {
        return new FacetedMethod(FeatureType.COLLECTION, declaringType, method, null, emptyParameterList());
    }

    public static FacetedMethod createActionFacetedMethod(final Class<?> declaringType, final Method method) {
        return new FacetedMethod(FeatureType.ACTION, declaringType, method, method.getReturnType(), getParameters(method));
    }

    private static List<FacetedMethodParameter> getParameters(final Method actionMethod) {
        final Class<?>[] parameterTypes = actionMethod.getParameterTypes();
        final List<FacetedMethodParameter> actionParams = Lists.newArrayList();
        for (final Class<?> parameterType : parameterTypes) {
            actionParams.add(new FacetedMethodParameter(parameterType));
        }
        return Collections.unmodifiableList(actionParams);
    }

    // //////////////////////////////////////////////////
    // Constructor
    // //////////////////////////////////////////////////

    private final Class<?> owningType;
    private final Method method;
    private final Identifier identifier;
    private final List<FacetedMethodParameter> parameters;

    public List<FacetedMethodParameter> getParameters() {
        return parameters;
    }

    public static List<FacetedMethodParameter> emptyParameterList() {
        final List<FacetedMethodParameter> emptyList = Collections.emptyList();
        return Collections.unmodifiableList(emptyList);
    }

    private FacetedMethod(final FeatureType featureType, final Class<?> declaringType, final Method method, final Class<?> type, final List<FacetedMethodParameter> parameters) {
        super(featureType, type);
        this.owningType = declaringType;
        this.method = method;
        this.identifier = featureType.identifierFor(declaringType, method);
        this.parameters = parameters;
    }

    /**
     * The {@link Class} that owns this {@link Method} (as per
     * {@link Class#getMethods()}, returning the {@link Method}s both of this
     * class and of its superclasses).
     * 
     * <p>
     * Note: we don't call this the 'declaring type' because
     * {@link Class#getDeclaredMethods()} does not return methods from
     * superclasses.
     */
    public Class<?> getOwningType() {
        return owningType;
    }

    /**
     * A {@link Method} obtained from the {@link #getOwningType() owning type}
     * using {@link Class#getMethods()}.
     */
    public Method getMethod() {
        return method;
    }

    @Override
    public Identifier getIdentifier() {
        return identifier;
    }

    // ////////////////////////////////////////////////////////////////////
    // toString, debug
    // ////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        return getFeatureType().name() + " Peer [identifier=\"" + getIdentifier() + "\",type=" + getType().getName() + " ]";
    }

    @Override
    public void debugData(final DebugBuilder debug) {
        // TODO: reinstate
        // debug.appendln("Identifier", identifier.toString());
    }

}
