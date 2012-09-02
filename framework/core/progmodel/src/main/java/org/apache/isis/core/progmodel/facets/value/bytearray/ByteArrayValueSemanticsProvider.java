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

import org.apache.isis.applib.profiles.Localization;
import org.apache.isis.core.commons.config.IsisConfiguration;
import org.apache.isis.core.metamodel.facetapi.Facet;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.progmodel.facets.object.value.ValueSemanticsProviderAndFacetAbstract;
import org.apache.isis.core.progmodel.facets.object.value.ValueSemanticsProviderContext;
import org.apache.isis.core.progmodel.facets.value.bytes.ByteValueFacet;

/**
 *
 * @author giedrius
 */
public class ByteArrayValueSemanticsProvider extends ValueSemanticsProviderAndFacetAbstract<byte[]> implements ByteArrayValueFacet {

    public ByteArrayValueSemanticsProvider(FacetHolder holder, IsisConfiguration configuration, ValueSemanticsProviderContext context) {
        super(ByteArrayValueFacet.class, holder, byte[].class, 1, false, true, null, configuration, context);
    }

    @Override
    protected String titleString(Object object, Localization localization) {
        return "binary";
    }

    @Override
    public String titleStringWithMask(Object value, String usingMask) {
        return "binary";
    }

    @Override
    protected String doEncode(Object object) {
        return new String((byte[]) object);
    }

    @Override
    protected byte[] doRestore(String data) {
        return data.getBytes();
    }
    
}
