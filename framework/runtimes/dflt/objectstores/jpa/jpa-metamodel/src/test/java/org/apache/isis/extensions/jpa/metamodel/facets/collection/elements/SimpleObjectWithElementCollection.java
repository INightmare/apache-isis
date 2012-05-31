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
package org.apache.isis.extensions.jpa.metamodel.facets.collection.elements;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.Id;

public class SimpleObjectWithElementCollection {

    private Long id;

    @Id
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    private List<SimpleObjectWithElementCollection> objects = new ArrayList<SimpleObjectWithElementCollection>();

    @ElementCollection(targetClass = SimpleObjectWithElementCollection.class, fetch=FetchType.LAZY)
    public List<SimpleObjectWithElementCollection> getObjects() {
        return objects;
    }

    public void setObjects(
            final List<SimpleObjectWithElementCollection> objects) {
        this.objects = objects;
    }

    private List<SimpleObjectWithElementCollection> otherObjects = 
        new ArrayList<SimpleObjectWithElementCollection>();

    public List<SimpleObjectWithElementCollection> getOtherObjects() {
        return otherObjects;
    }

    public void setOtherObjects(
            final List<SimpleObjectWithElementCollection> otherObjects) {
        this.otherObjects = otherObjects;
    }

}