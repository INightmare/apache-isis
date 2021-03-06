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

package org.apache.isis.core.commons.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.isis.core.commons.lang.IoUtils;
import org.apache.isis.core.commons.resource.ResourceStreamSource;

/**
 * Loads properties using the specified {@link ResourceStreamSource}.
 */
class PropertiesReader {

    private final Properties properties = new Properties();

    public PropertiesReader(final ResourceStreamSource resourceStream, final String configurationResource) throws IOException {

        InputStream in = null;
        try {
            in = resourceStream.readResource(configurationResource);
            if (in == null) {
                throw new IOException("Unable to find resource " + configurationResource);
            }
            properties.load(in);
        } finally {
            IoUtils.closeSafely(in);
        }
    }

    public Properties getProperties() {
        return properties;
    }

}
