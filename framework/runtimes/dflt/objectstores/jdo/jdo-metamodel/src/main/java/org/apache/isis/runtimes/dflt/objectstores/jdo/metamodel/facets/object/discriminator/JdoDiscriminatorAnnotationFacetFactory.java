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

package org.apache.isis.runtimes.dflt.objectstores.jdo.metamodel.facets.object.discriminator;

import javax.jdo.annotations.Discriminator;

import org.apache.isis.core.metamodel.facetapi.FacetUtil;
import org.apache.isis.core.metamodel.facetapi.FeatureType;
import org.apache.isis.core.metamodel.facets.AnnotationBasedFacetFactoryAbstract;

public class JdoDiscriminatorAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract {

    public JdoDiscriminatorAnnotationFacetFactory() {
        super(FeatureType.OBJECTS_ONLY);
    }

    @Override
    public void process(ProcessClassContext processClassContext) {

        final Discriminator annotation = getAnnotation(processClassContext.getCls(), Discriminator.class);
        if (annotation == null) {
            return;
        }
        final String annotationValueAttribute = annotation.value();

        FacetUtil.addFacet(new ObjectSpecIdFacetInferredFromJdoDiscriminatorValueAnnotation(annotationValueAttribute, processClassContext.getFacetHolder()));
        FacetUtil.addFacet(new JdoDiscriminatorFacetDefault(annotationValueAttribute, processClassContext.getFacetHolder()));
    }

}
