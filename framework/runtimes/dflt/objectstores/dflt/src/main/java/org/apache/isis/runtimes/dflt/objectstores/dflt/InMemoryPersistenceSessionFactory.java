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

package org.apache.isis.runtimes.dflt.objectstores.dflt;

import org.apache.isis.runtimes.dflt.objectstores.dflt.internal.ObjectStoreInstances;
import org.apache.isis.runtimes.dflt.objectstores.dflt.internal.ObjectStorePersistedObjects;
import org.apache.isis.runtimes.dflt.objectstores.dflt.internal.ObjectStorePersistedObjectsDefault;
import org.apache.isis.runtimes.dflt.runtime.persistence.PersistenceSessionFactoryDelegate;
import org.apache.isis.runtimes.dflt.runtime.persistence.PersistenceSessionFactoryDelegating;
import org.apache.isis.runtimes.dflt.runtime.system.DeploymentType;
import org.apache.isis.runtimes.dflt.runtime.system.persistence.IdentifierGenerator;
import org.apache.isis.runtimes.dflt.runtime.system.persistence.IdentifierGeneratorDefault;
import org.apache.isis.runtimes.dflt.runtime.system.persistence.OidGenerator;
import org.apache.isis.runtimes.dflt.runtime.system.persistence.PersistenceSession;

public class InMemoryPersistenceSessionFactory extends PersistenceSessionFactoryDelegating {

    private ObjectStorePersistedObjects persistedObjects;

    public InMemoryPersistenceSessionFactory(final DeploymentType deploymentType, final PersistenceSessionFactoryDelegate persistenceSessionFactoryDelegate) {
        super(deploymentType, persistenceSessionFactoryDelegate);
    }

    protected ObjectStorePersistedObjects getPersistedObjects() {
        return persistedObjects;
    }

    @Override
    public PersistenceSession createPersistenceSession() {
        final PersistenceSession persistenceSession = super.createPersistenceSession();
        if (persistedObjects != null) {
            final OidGenerator oidGenerator = persistenceSession.getOidGenerator();
            final IdentifierGenerator identifierGenerator = oidGenerator.getIdentifierGenerator();
            if (identifierGenerator instanceof IdentifierGeneratorDefault) {
                final IdentifierGeneratorDefault identifierGeneratorDefault = (IdentifierGeneratorDefault) identifierGenerator;
                identifierGeneratorDefault.resetTo(persistedObjects.getOidGeneratorMemento());
            }
        }

        return persistenceSession;
    }

    /**
     * Not API - called when {@link InMemoryObjectStore} first
     * {@link InMemoryObjectStore#open() open}ed.
     */
    public ObjectStorePersistedObjects createPersistedObjects() {
        return new ObjectStorePersistedObjectsDefault();
    }

    /**
     * Not API - called when {@link InMemoryObjectStore} is
     * {@link InMemoryObjectStore#close() close}d.
     */
    public void attach(final PersistenceSession persistenceSession, final ObjectStorePersistedObjects persistedObjects) {
        final OidGenerator oidGenerator = persistenceSession.getOidGenerator();
        final IdentifierGenerator identifierGenerator = oidGenerator.getIdentifierGenerator();
        if (identifierGenerator instanceof IdentifierGeneratorDefault) {
            final IdentifierGeneratorDefault identifierGeneratorDefault = (IdentifierGeneratorDefault) identifierGenerator;
            persistedObjects.saveOidGeneratorMemento(identifierGeneratorDefault.getMemento());
        }
        this.persistedObjects = persistedObjects;
    }

    @Override
    protected void doShutdown() {
        if (persistedObjects != null) {
            for (final ObjectStoreInstances inst : persistedObjects.instances()) {
                inst.shutdown();
            }
            persistedObjects.clear();
        }
    }

}
