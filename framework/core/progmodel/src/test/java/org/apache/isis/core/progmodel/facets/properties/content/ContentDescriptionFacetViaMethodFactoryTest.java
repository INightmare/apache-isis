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
package org.apache.isis.core.progmodel.facets.properties.content;

import java.lang.reflect.Method;
import org.apache.isis.applib.content.ContentDescription;
import org.apache.isis.applib.content.DefaultContentDescription;
import org.apache.isis.core.metamodel.facets.FacetFactory;
import org.apache.isis.core.progmodel.facets.AbstractFacetFactoryTest;
import org.apache.isis.core.progmodel.facets.properties.lazy.LazyLoadedPropertyFacet;
import org.apache.isis.core.progmodel.facets.properties.lazy.LazyLoadedPropertyFacetFactoryTest;

/**
 *
 * @author giedrius
 */
public class ContentDescriptionFacetViaMethodFactoryTest extends AbstractFacetFactoryTest {
    private FacetFactory facetFactory = new ContentDescriptionFacetViaMethodFactory();
    
    public void testDescriptionFacetIsAdded() {
        final Method method = findMethod(ContentDescriptionFacetViaMethodFactoryTest.Attachment.class, "getContent");
        facetFactory.process(new FacetFactory.ProcessMethodContext(ContentDescriptionFacetViaMethodFactoryTest.Attachment.class, method, methodRemover, facetedMethod));
        ContentDescriptionFacetViaMethod facet = facetedMethod.getFacet(ContentDescriptionFacetViaMethod.class);
        assertNotNull(facet);
    }
    
    public class Attachment {
        private byte[] content;
        
        public byte[] getContent() {
            return content;
        }
        
        public ContentDescription describeContent() {
            return new DefaultContentDescription("a", "b");
        }
    }
}
