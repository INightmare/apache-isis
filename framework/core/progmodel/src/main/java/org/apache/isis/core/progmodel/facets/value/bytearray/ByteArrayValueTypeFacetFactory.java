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
package org.apache.isis.core.progmodel.facets.value.bytearray;

import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.progmodel.facets.object.value.ValueUsingValueSemanticsProviderFacetFactory;

/**
 *
 * @author giedrius
 */
public class ByteArrayValueTypeFacetFactory extends ValueUsingValueSemanticsProviderFacetFactory<byte[]> {
    public ByteArrayValueTypeFacetFactory() {
        super(ByteArrayValueFacet.class);
    }
    
    @Override
    public void process(final ProcessClassContext processClassContext) {
        final Class<?> type = processClassContext.getCls();
        final FacetHolder holder = processClassContext.getFacetHolder();

        if (type != byte[].class) {
            return;
        }
        addFacets(new ByteArrayValueSemanticsProvider(holder, getConfiguration(), getContext()));
    }
}
