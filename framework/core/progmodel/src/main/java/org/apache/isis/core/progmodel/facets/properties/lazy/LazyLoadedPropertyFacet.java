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

import org.apache.isis.core.metamodel.facetapi.Facet;

/**
 *
 * Marks the property as lazy loaded.
 * 
 * Thus:
 * <ul>
 * <li>Viewer implementations should not access this property in cases
 * where there are multiple instances loaded (lists, etc). Generally there
 * this Facet should mean that property holds data that should be fetched
 * only if user specifically requested it.</li>
 * <li>Any object store implementations should not load the property together
 * with containing object.</li>
 * </ul>
 */
public interface LazyLoadedPropertyFacet extends Facet {
    
}
