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

package org.apache.isis.core.progmodel.facets.properties.validate.maxlenannot;

import org.apache.isis.applib.annotation.MaxLength;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.metamodel.facetapi.FacetUtil;
import org.apache.isis.core.metamodel.facetapi.FeatureType;
import org.apache.isis.core.metamodel.facets.AnnotationBasedFacetFactoryAbstract;
import org.apache.isis.core.metamodel.facets.maxlen.MaxLengthFacet;

public class MaxLengthAnnotationForPropertyFacetFactory extends AnnotationBasedFacetFactoryAbstract {

    public MaxLengthAnnotationForPropertyFacetFactory() {
        super(FeatureType.PROPERTIES_ONLY);
    }

    @Override
    public void process(final ProcessMethodContext processMethodContext) {
        final MaxLength annotation = getAnnotation(processMethodContext.getMethod(), MaxLength.class);
        FacetUtil.addFacet(create(annotation, processMethodContext.getFacetHolder()));
    }

    private MaxLengthFacet create(final MaxLength annotation, final FacetHolder holder) {
        return annotation == null ? null : new MaxLengthFacetAnnotationForProperty(annotation.value(), holder);
    }
}
