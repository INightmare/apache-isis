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
import org.apache.isis.core.commons.lang.NameUtils;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.metamodel.facetapi.FacetUtil;
import org.apache.isis.core.metamodel.facetapi.FeatureType;
import org.apache.isis.core.metamodel.methodutils.MethodScope;
import org.apache.isis.core.progmodel.facets.MethodFinderUtils;
import org.apache.isis.core.progmodel.facets.MethodPrefixBasedFacetFactoryAbstract;
import org.apache.isis.core.progmodel.facets.MethodPrefixConstants;

/**
 *
 * @author giedrius
 */
public class ContentDescriptionFacetViaMethodFactory extends MethodPrefixBasedFacetFactoryAbstract {
    
    private static final String[] PREFIXES = { MethodPrefixConstants.DESCRIBE_PREFIX };
    
    public ContentDescriptionFacetViaMethodFactory() {
        super(FeatureType.MEMBERS, PREFIXES);
    }
    
    public void process(final ProcessMethodContext processMethodContext) {
        final Class cls = processMethodContext.getCls();
        final Method method = processMethodContext.getMethod();
        
        final String capitalizedName = NameUtils.javaBaseNameStripAccessorPrefixIfRequired(method.getName());
        
        System.out.println(capitalizedName);
        
        final Method describeMethod = MethodFinderUtils.findMethod(cls, MethodScope.OBJECT, MethodPrefixConstants.DESCRIBE_PREFIX + capitalizedName, ContentDescription.class, new Class[] {});
        
        if (describeMethod == null) {
            return;
        }
        
        processMethodContext.removeMethod(describeMethod);

        final FacetHolder facetHolder = processMethodContext.getFacetHolder();
        FacetUtil.addFacet(new ContentDescriptionFacetViaMethod(describeMethod, facetHolder));
    }
}
