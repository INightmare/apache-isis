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

import org.apache.isis.core.metamodel.facetapi.Facet;
import org.apache.isis.core.metamodel.facetapi.FacetAbstract;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;

public abstract class SingleWhenValueFacetAbstract extends FacetAbstract implements SingleWhenValueFacet {
    private final When value;

    public SingleWhenValueFacetAbstract(final Class<? extends Facet> facetType, final FacetHolder holder, final When value) {
        super(facetType, holder, Derivation.NOT_DERIVED);
        this.value = value;
    }

    @Override
    public When value() {
        return value;
    }

    /**
     * More descriptive, same as {@link #value()}.
     * 
     * @return
     */
    protected When when() {
        return value();
    }

    @Override
    protected String toStringValues() {
        return "when=" + value.getFriendlyName();
    }
}
