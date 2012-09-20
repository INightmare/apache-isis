/*
 * Copyright 2012 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.isis.core.progmodel.facets.properties.lazy;

import java.lang.reflect.Method;
import org.apache.isis.applib.annotation.Lazy;
import org.apache.isis.core.metamodel.facetapi.FacetUtil;
import org.apache.isis.core.metamodel.facetapi.FeatureType;
import org.apache.isis.core.metamodel.facets.AnnotationBasedFacetFactoryAbstract;

/**
 *
 * Adds facet to properties of type byte[] and properties annotated with @Lazy
 */
public class LazyPropertyFacetFactory extends AnnotationBasedFacetFactoryAbstract {
    
    public LazyPropertyFacetFactory() {
        super(FeatureType.PROPERTIES_ONLY);
    }
    
    @Override
    public void process(final ProcessMethodContext processMethodContext) {
        final Method method = processMethodContext.getMethod();
        final Class<?> cls = method.getReturnType();
        Lazy lazy = getAnnotation(method, Lazy.class);
        
        if ( (lazy == null && cls == byte[].class)
                || (lazy != null && lazy.value()) ) {
            FacetUtil.addFacet(new LazyPropertyFacetDefault(processMethodContext.getFacetHolder()));
        }
        
    }
    
}
