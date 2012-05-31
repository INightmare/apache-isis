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
package org.apache.isis.extensions.jpa.metamodel.facets.object.namedquery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.isis.core.metamodel.facetapi.Facet;
import org.apache.isis.core.metamodel.facetapi.FacetAbstract;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.metamodel.spec.ObjectSpecification;


public class JpaNamedQueryFacetAbstract extends FacetAbstract implements
        JpaNamedQueryFacet {

    public static Class<? extends Facet> type() {
        return JpaNamedQueryFacet.class;
    }

    private final List<NamedQuery> namedQueries = new ArrayList<NamedQuery>();

    public JpaNamedQueryFacetAbstract(final FacetHolder holder) {
        super(JpaNamedQueryFacetAbstract.type(), holder, false);
    }

    protected void add(final javax.persistence.NamedQuery... jpaNamedQueries) {
        final ObjectSpecification objSpec = (ObjectSpecification) getFacetHolder();
        for (final javax.persistence.NamedQuery jpaNamedQuery : jpaNamedQueries) {
            namedQueries.add(new NamedQuery(jpaNamedQuery, objSpec));
        }
    }

    public List<NamedQuery> getNamedQueries() {
        return Collections.unmodifiableList(namedQueries);
    }

}
