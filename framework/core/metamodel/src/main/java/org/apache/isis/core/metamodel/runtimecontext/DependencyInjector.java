/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.isis.core.metamodel.runtimecontext;

import java.util.List;

import org.apache.isis.core.commons.components.Injectable;

public interface DependencyInjector extends Injectable {

    /**
     * Provided by the <tt>ServicesInjectorDefault</tt> when used by framework.
     * 
     * <p>
     * Called in multiple places from metamodel and facets.
     */
    void injectDependenciesInto(final Object domainObject);

    /**
     * As per {@link #injectDependenciesInto(Object)}, but for all objects in the
     * list.
     */
    void injectDependenciesInto(List<Object> objects);

}
