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

package org.apache.isis.core.progmodel.facets.paged;

import org.apache.isis.applib.annotation.Paged;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.metamodel.facetapi.FacetUtil;
import org.apache.isis.core.metamodel.facetapi.FeatureType;
import org.apache.isis.core.metamodel.facets.AnnotationBasedFacetFactoryAbstract;
import org.apache.isis.core.metamodel.facets.object.paged.PagedFacet;

public class PagedAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract {

    public PagedAnnotationFacetFactory() {
        super(FeatureType.OBJECTS_AND_COLLECTIONS);
    }

    @Override
    public void process(final ProcessClassContext processClassContext) {
        final Paged annotation = getAnnotation(processClassContext.getCls(), Paged.class);
        FacetUtil.addFacet(create(annotation, processClassContext.getFacetHolder()));
    }

    @Override
    public void process(final ProcessMethodContext processMethodContext) {
        final Paged annotation = getAnnotation(processMethodContext.getMethod(), Paged.class);
        FacetUtil.addFacet(create(annotation, processMethodContext.getFacetHolder()));
    }

    private PagedFacet create(final Paged annotation, final FacetHolder holder) {
        return annotation == null ? null : new PagedFacetAnnotation(annotation.value(), annotation.value(), holder);
    }

}
