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
package org.apache.isis.runtimes.dflt.objectstores.jdo.datanucleus.metamodel.specloader.progmodelfacets;


import org.apache.isis.core.progmodel.facets.object.ignore.jdo.RemoveJdoEnhancementTypesFacetFactory;
import org.apache.isis.core.progmodel.facets.object.ignore.jdo.RemoveJdoPrefixedMethodsFacetFactory;
import org.apache.isis.progmodels.dflt.ProgrammingModelFacetsJava5;
import org.apache.isis.runtimes.dflt.objectstores.jdo.metamodel.specloader.progmodelfacets.JdoProgrammingModelFacets;

/**
 * As per the {@link ProgrammingModelFacetsJava5 Java 5 default programming
 * model}, but also
 * includes support for JPA.
 * <p>
 * Intended to be used by the {@link JpaJavaReflectorInstaller}, which
 * additionally sets up other required components needed for JPA support.
 */
public class DataNucleusProgrammingModelFacets extends JdoProgrammingModelFacets {

    public DataNucleusProgrammingModelFacets() {
        addFactory(RemoveJdoEnhancementTypesFacetFactory.class);
        addFactory(RemoveJdoPrefixedMethodsFacetFactory.class);
    }

}


// Copyright (c) Naked Objects Group Ltd.
