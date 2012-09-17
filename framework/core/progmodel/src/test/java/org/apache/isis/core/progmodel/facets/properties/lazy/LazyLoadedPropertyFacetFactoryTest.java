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
import org.apache.isis.applib.filter.Filter;
import org.apache.isis.applib.filter.Filters;
import org.apache.isis.core.metamodel.facetapi.Facet;
import org.apache.isis.core.metamodel.facets.FacetFactory;
import org.apache.isis.core.metamodel.facets.FacetFactory.ProcessMethodContext;
import org.apache.isis.core.progmodel.facets.AbstractFacetFactoryTest;

/**
 *
 * @author giedrius
 */
public class LazyLoadedPropertyFacetFactoryTest extends AbstractFacetFactoryTest {
    
    private FacetFactory facetFactory = new LazyLoadedPropertyFacetFactory();
    
    public void testLazyLoadedPropertyFacetIsAdded() {
        assertNotNull("should add facet for byte[] property", checkFacetForMethod("getPhoto"));
        assertNotNull("should add facet for property marked with @Lazy", checkFacetForMethod("getDescription"));
    }
    
    public void testLazyLoadedPropertyFacetIsNotAdded() {
        assertNull("should not add facet for simple property", checkFacetForMethod("getName"));
        assertNull("should not add facet for property marked with @Lazy(false)", checkFacetForMethod("getSignature"));
    }
    
    
    private Facet checkFacetForMethod(String methodName) {
        final Method method = findMethod(LazyLoadedPropertyFacetFactoryTest.Customer.class, methodName);
        facetFactory.process(new ProcessMethodContext(LazyLoadedPropertyFacetFactoryTest.Customer.class, method, methodRemover, facetedMethod));
        return facetedMethod.getFacet(LazyLoadedPropertyFacet.class);
    }
    
    class Customer {

        public String getName() {
            return "";
        }
        
        public byte[] getPhoto() {
            return null;
        }

        @Lazy
        public String getDescription() {
            return "";
        }
        
        @Lazy(false)
        public byte[] getSignature() {
            return null;
        }
    }
}
