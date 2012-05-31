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
package org.apache.isis.extensions.jpa.metamodel.facets.prop.manytoone;

import javax.persistence.ManyToOne;

import org.apache.isis.core.metamodel.facets.MarkerFacet;


/**
 * Corresponds to the property with the {@link ManyToOne} annotation.
 * <p>
 * Maps onto the information in {@link ManyToOne} as follows:
 * <ul>
 * <li>{@link ManyToOne#targetEntity()} -> (no corresponding attribute or facet)
 * </li>
 * <li>{@link ManyToOne#cascade()} -> (no corresponding attribute or facet)</li>
 * <li>{@link ManyToOne#fetch()} ->
 * {@link JpaFetchTypeFacetDerivedFromJpaManyToOneAnnotation}</li>
 * <li>{@link ManyToOne#optional()} ->
 * {@link MandatoryFacetDerivedFromJpaManyToOneAnnotation} or
 * {@link MandatoryFacetDerivedFromJpaManyToOneAnnotation}</li>
 * </ul>
 */
public interface JpaManyToOneFacet extends MarkerFacet {

}
