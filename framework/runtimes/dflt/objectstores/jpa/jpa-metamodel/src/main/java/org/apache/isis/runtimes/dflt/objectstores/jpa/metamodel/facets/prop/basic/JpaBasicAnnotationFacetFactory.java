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
package org.apache.isis.runtimes.dflt.objectstores.jpa.metamodel.facets.prop.basic;

import javax.persistence.Basic;

import org.apache.isis.core.metamodel.facetapi.FacetUtil;
import org.apache.isis.core.metamodel.facetapi.FeatureType;
import org.apache.isis.core.metamodel.facets.AnnotationBasedFacetFactoryAbstract;
import org.apache.isis.core.metamodel.facets.FacetedMethod;


public class JpaBasicAnnotationFacetFactory extends
        AnnotationBasedFacetFactoryAbstract {

    public JpaBasicAnnotationFacetFactory() {
        super(FeatureType.PROPERTIES_ONLY);
    }

    @Override
    public void process(ProcessMethodContext processMethodContext) {
        
        final Basic annotation = getAnnotation(processMethodContext.getMethod(), Basic.class);
        if (annotation == null) {
            return ;
        }
        
        final FacetedMethod holder = processMethodContext.getFacetHolder();
        FacetUtil.addFacet(new JpaBasicFacetAnnotation(holder));
        if (annotation.optional()) {
            // ie, a MandatoryFacet implementation with inverted semantics
            FacetUtil.addFacet(new OptionalFacetDerivedFromJpaBasicAnnotation(
                    holder));
        } else {
            FacetUtil.addFacet(new MandatoryFacetDerivedFromJpaBasicAnnotation(
                    holder));
        }
        FacetUtil.addFacet(new JpaFetchTypeFacetDerivedFromJpaBasicAnnotation(
                holder, annotation.fetch()));
    }


}
