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

package org.apache.isis.core.progmodel.facets.value.date;

import java.util.Date;

import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.facetapi.Facet;

public interface DateValueFacet extends Facet {
    public static final int DATE = 0x01;
    public static final int TIME = 0x10;
    public static final int PRECISION = 0x100;

    public static final int DATE_ONLY = DATE;
    public static final int TIME_ONLY = TIME;
    public static final int DATE_AND_TIME = DATE + TIME;
    public static final int TIMESTAMP = DATE + TIME + PRECISION;

    Date dateValue(ObjectAdapter object);

    ObjectAdapter createValue(Date date);

    int getLevel();

}
