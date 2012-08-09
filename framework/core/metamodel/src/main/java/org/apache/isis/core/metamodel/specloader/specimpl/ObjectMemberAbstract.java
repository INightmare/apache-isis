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

package org.apache.isis.core.metamodel.specloader.specimpl;

import java.util.List;

import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.filter.Filter;
import org.apache.isis.core.commons.authentication.AuthenticationSession;
import org.apache.isis.core.commons.authentication.AuthenticationSessionProvider;
import org.apache.isis.core.commons.lang.NameUtils;
import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.adapter.QuerySubmitter;
import org.apache.isis.core.metamodel.adapter.map.AdapterManager;
import org.apache.isis.core.metamodel.consent.Consent;
import org.apache.isis.core.metamodel.consent.InteractionInvocationMethod;
import org.apache.isis.core.metamodel.consent.InteractionResult;
import org.apache.isis.core.metamodel.facetapi.Facet;
import org.apache.isis.core.metamodel.facetapi.FeatureType;
import org.apache.isis.core.metamodel.facetapi.MultiTypedFacet;
import org.apache.isis.core.metamodel.facets.FacetedMethod;
import org.apache.isis.core.metamodel.facets.describedas.DescribedAsFacet;
import org.apache.isis.core.metamodel.facets.help.HelpFacet;
import org.apache.isis.core.metamodel.facets.hide.HiddenFacet;
import org.apache.isis.core.metamodel.facets.named.NamedFacet;
import org.apache.isis.core.metamodel.interactions.DisablingInteractionAdvisor;
import org.apache.isis.core.metamodel.interactions.HidingInteractionAdvisor;
import org.apache.isis.core.metamodel.interactions.InteractionUtils;
import org.apache.isis.core.metamodel.interactions.UsabilityContext;
import org.apache.isis.core.metamodel.interactions.VisibilityContext;
import org.apache.isis.core.metamodel.spec.ObjectSpecification;
import org.apache.isis.core.metamodel.spec.SpecificationLoader;
import org.apache.isis.core.metamodel.spec.feature.ObjectMember;
import org.apache.isis.core.metamodel.spec.feature.ObjectMemberContext;
import org.apache.isis.core.metamodel.specloader.collectiontyperegistry.CollectionTypeRegistry;

public abstract class ObjectMemberAbstract implements ObjectMember {

    public static ObjectSpecification getSpecification(final SpecificationLoader specificationLookup, final Class<?> type) {
        return type == null ? null : specificationLookup.loadSpecification(type);
    }

    protected final String defaultName;
    private final String id;
    private final FacetedMethod facetedMethod;
    private final FeatureType featureType;
    private final AuthenticationSessionProvider authenticationSessionProvider;
    private final SpecificationLoader specificationLookup;
    private final AdapterManager adapterMap;
    private final QuerySubmitter querySubmitter;
    private final CollectionTypeRegistry collectionTypeRegistry;

    protected ObjectMemberAbstract(final FacetedMethod facetedMethod, final FeatureType featureType, final ObjectMemberContext objectMemberContext) {
        final String id = facetedMethod.getIdentifier().getMemberName();
        if (id == null) {
            throw new IllegalArgumentException("Name must always be set");
        }
        this.facetedMethod = facetedMethod;
        this.featureType = featureType;
        this.id = id;
        this.defaultName = NameUtils.naturalName(this.id);

        this.authenticationSessionProvider = objectMemberContext.getAuthenticationSessionProvider();
        this.specificationLookup = objectMemberContext.getSpecificationLookup();
        this.adapterMap = objectMemberContext.getAdapterManager();
        this.querySubmitter = objectMemberContext.getQuerySubmitter();
        this.collectionTypeRegistry = objectMemberContext.getCollectionTypeRegistry();
    }

    // /////////////////////////////////////////////////////////////
    // Identifiers
    // /////////////////////////////////////////////////////////////

    @Override
    public String getId() {
        return id;
    }

    /**
     * @return the facetedMethod
     */
    public FacetedMethod getFacetedMethod() {
        return facetedMethod;
    }

    @Override
    public Identifier getIdentifier() {
        return getFacetedMethod().getIdentifier();
    }

    @Override
    public FeatureType getFeatureType() {
        return featureType;
    }

    // /////////////////////////////////////////////////////////////
    // Facets
    // /////////////////////////////////////////////////////////////

    @Override
    public boolean containsFacet(final Class<? extends Facet> facetType) {
        return getFacetedMethod().containsFacet(facetType);
    }

    @Override
    public boolean containsDoOpFacet(final Class<? extends Facet> facetType) {
        return getFacetedMethod().containsDoOpFacet(facetType);
    }

    @Override
    public <T extends Facet> T getFacet(final Class<T> cls) {
        return getFacetedMethod().getFacet(cls);
    }

    @Override
    public Class<? extends Facet>[] getFacetTypes() {
        return getFacetedMethod().getFacetTypes();
    }

    @Override
    public List<Facet> getFacets(final Filter<Facet> filter) {
        return getFacetedMethod().getFacets(filter);
    }

    @Override
    public void addFacet(final Facet facet) {
        getFacetedMethod().addFacet(facet);
    }

    @Override
    public void addFacet(final MultiTypedFacet facet) {
        getFacetedMethod().addFacet(facet);
    }

    @Override
    public void removeFacet(final Facet facet) {
        getFacetedMethod().removeFacet(facet);
    }

    @Override
    public void removeFacet(final Class<? extends Facet> facetType) {
        getFacetedMethod().removeFacet(facetType);
    }

    // /////////////////////////////////////////////////////////////
    // Name, Description, Help (convenience for facets)
    // /////////////////////////////////////////////////////////////

    /**
     * Return the default label for this member. This is based on the name of
     * this member.
     * 
     * @see #getId()
     */
    @Override
    public String getName() {
        final NamedFacet facet = getFacet(NamedFacet.class);
        final String name = facet.value();
        return name != null ? name : defaultName;
    }

    @Override
    public String getDescription() {
        final DescribedAsFacet facet = getFacet(DescribedAsFacet.class);
        return facet.value();
    }

    @Override
    public String getHelp() {
        final HelpFacet facet = getFacet(HelpFacet.class);
        return facet.value();
    }

    // /////////////////////////////////////////////////////////////
    // Hidden (or visible)
    // /////////////////////////////////////////////////////////////

    @Override
    public boolean isAlwaysHidden() {
        return containsFacet(HiddenFacet.class);
    }

    /**
     * Loops over all {@link HidingInteractionAdvisor} {@link Facet}s and
     * returns <tt>true</tt> only if none hide the member.
     * 
     * <p>
     * TODO: currently this method is hard-coded to assume all interactions are
     * initiated {@link InteractionInvocationMethod#BY_USER by user}.
     */
    @Override
    public Consent isVisible(final AuthenticationSession session, final ObjectAdapter target) {
        return isVisibleResult(session, target).createConsent();
    }

    private InteractionResult isVisibleResult(final AuthenticationSession session, final ObjectAdapter target) {
        final VisibilityContext<?> ic = createVisibleInteractionContext(session, InteractionInvocationMethod.BY_USER, target);
        return InteractionUtils.isVisibleResult(this, ic);
    }

    // /////////////////////////////////////////////////////////////
    // Disabled (or enabled)
    // /////////////////////////////////////////////////////////////

    /**
     * Loops over all {@link DisablingInteractionAdvisor} {@link Facet}s and
     * returns <tt>true</tt> only if none disables the member.
     * 
     * <p>
     * TODO: currently this method is hard-coded to assume all interactions are
     * initiated {@link InteractionInvocationMethod#BY_USER by user}.
     */
    @Override
    public Consent isUsable(final AuthenticationSession session, final ObjectAdapter target) {
        return isUsableResult(session, target).createConsent();
    }

    private InteractionResult isUsableResult(final AuthenticationSession session, final ObjectAdapter target) {
        final UsabilityContext<?> ic = createUsableInteractionContext(session, InteractionInvocationMethod.BY_USER, target);
        return InteractionUtils.isUsableResult(this, ic);
    }

    // //////////////////////////////////////////////////////////////////
    // isAssociation, isAction
    // //////////////////////////////////////////////////////////////////

    @Override
    public boolean isAction() {
        return featureType.isAction();
    }

    @Override
    public boolean isPropertyOrCollection() {
        return featureType.isPropertyOrCollection();
    }

    @Override
    public boolean isOneToManyAssociation() {
        return featureType.isCollection();
    }

    @Override
    public boolean isOneToOneAssociation() {
        return featureType.isProperty();
    }

    // //////////////////////////////////////////////////////////////////
    // Convenience
    // //////////////////////////////////////////////////////////////////

    /**
     * The current {@link AuthenticationSession} (can change over time so do not
     * cache).
     */
    protected AuthenticationSession getAuthenticationSession() {
        return authenticationSessionProvider.getAuthenticationSession();
    }

    // //////////////////////////////////////////////////////////////////
    // toString
    // //////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        return String.format("id=%s,name='%s'", getId(), getName());
    }

    // //////////////////////////////////////////////////////////////////
    // Dependencies
    // //////////////////////////////////////////////////////////////////

    public AuthenticationSessionProvider getAuthenticationSessionProvider() {
        return authenticationSessionProvider;
    }

    public SpecificationLoader getSpecificationLookup() {
        return specificationLookup;
    }

    public AdapterManager getAdapterMap() {
        return adapterMap;
    }

    public QuerySubmitter getQuerySubmitter() {
        return querySubmitter;
    }

    public CollectionTypeRegistry getCollectionTypeRegistry() {
        return collectionTypeRegistry;
    }
}
