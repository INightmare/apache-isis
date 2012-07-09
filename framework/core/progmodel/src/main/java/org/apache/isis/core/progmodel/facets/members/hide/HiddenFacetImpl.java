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

package org.apache.isis.core.progmodel.facets.members.hide;

import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.metamodel.facets.When;

public class HiddenFacetImpl extends HiddenFacetAbstract {

    public HiddenFacetImpl(final When value, final FacetHolder holder) {
        super(value, holder);
    }

    @Override
    public String hiddenReason(final ObjectAdapter targetAdapter) {
        if (value() == When.ALWAYS) {
            return "Always hidden";
        } else if (value() == When.NEVER) {
            return null;
        }

        // remaining tests depend on target in question.
        if (targetAdapter == null) {
            return null;
        }

        if (value() == When.UNTIL_PERSISTED) {
            return targetAdapter.isTransient() ? "Hidden until persisted" : null;
        } else if (value() == When.ONCE_PERSISTED) {
            return targetAdapter.representsPersistent() ? "Hidden once persisted" : null;
        }
        return null;
    }

}
