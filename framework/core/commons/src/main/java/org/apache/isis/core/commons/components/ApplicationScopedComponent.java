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

package org.apache.isis.core.commons.components;

/**
 * Indicate that the implementing component is scoped at application level
 * (shared across multiple sessions) and might also require initialization or
 * being shutdown.
 * 
 * <p>
 * Analogous to Hibernate's <tt>SessionFactory</tt>.
 * 
 * @see SessionScopedComponent
 * @see TransactionScopedComponent
 */
public interface ApplicationScopedComponent extends Component {

    /**
     * Indicates to the component that it is to initialise itself.
     */
    void init();

    /**
     * Indicates to the component that it will no longer be used and should shut
     * itself down cleanly.
     */
    void shutdown();

}
