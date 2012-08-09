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

package org.apache.isis.viewer.scimpi.dispatcher.context;

import org.apache.isis.core.commons.debug.DebugString;
import org.apache.isis.core.commons.ensure.Assert;
import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.adapter.oid.Oid;
import org.apache.isis.core.metamodel.adapter.oid.RootOid;
import org.apache.isis.core.metamodel.spec.ObjectSpecification;
import org.apache.isis.runtimes.dflt.runtime.memento.Memento;
import org.apache.isis.runtimes.dflt.runtime.persistence.adaptermanager.AdapterManagerExtended;
import org.apache.isis.runtimes.dflt.runtime.system.context.IsisContext;
import org.apache.isis.runtimes.dflt.runtime.system.persistence.PersistenceSession;

interface Mapping {
    ObjectAdapter getObject();

    Oid getOid();

    String debug();

    void reload();

    void update();
}

class TransientRootAdapterMapping implements Mapping {
    private final RootOid oid;
    private Memento memento;

    public TransientRootAdapterMapping(final ObjectAdapter adapter) {
        oid = (RootOid) adapter.getOid();
        Assert.assertTrue("OID is for persistent", oid.isTransient());
        Assert.assertTrue("adapter is for persistent", adapter.isTransient());
        memento = new Memento(adapter);
    }

    @Override
    public ObjectAdapter getObject() {
        return IsisContext.getPersistenceSession().getAdapterManager().getAdapterFor(oid);
    }

    @Override
    public Oid getOid() {
        return oid;
    }

    @Override
    public String debug() {
        final DebugString debug = new DebugString();
        memento.debug(debug);
        return debug.toString();
    }

    @Override
    public void reload() {
        memento.recreateObject();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof TransientRootAdapterMapping) {
            return ((TransientRootAdapterMapping) obj).oid.equals(oid);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return oid.hashCode();
    }

    @Override
    public void update() {
        memento = new Memento(getObject());
    }
}

class PersistentRootAdapterMapping implements Mapping {
    private final RootOid oid;
    private final ObjectSpecification spec;

    public PersistentRootAdapterMapping(final ObjectAdapter object) {
        this.oid = (RootOid) object.getOid();
        this.spec = object.getSpecification();
    }

    @Override
    public Oid getOid() {
        return oid;
    }

    @Override
    public String debug() {
        return oid + "  " + spec.getShortIdentifier() + "  " + getAdapterManager().getAdapterFor(oid);
    }

    @Override
    public ObjectAdapter getObject() {
        if (!IsisContext.inTransaction()) {
            throw new IllegalStateException(getClass().getSimpleName() + " requires transaction in order to load");
        }
        return getPersistenceSession().loadObject(oid);
    }

    @Override
    public void reload() {
        if (getAdapterManager().getAdapterFor(oid) == null) {
            final Object recreatedPojo = spec.createObject();
            getAdapterManager().mapRecreatedPojo(oid, recreatedPojo);
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof PersistentRootAdapterMapping) {
            final PersistentRootAdapterMapping other = (PersistentRootAdapterMapping) obj;
            return oid.equals(other.oid) && spec == other.spec;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 37;
        hash = hash * 17 + oid.hashCode();
        hash = hash * 17 + spec.hashCode();
        return hash;
    }

    @Override
    public void update() {
    }

    
    ////////////////////////////////////
    

    protected PersistenceSession getPersistenceSession() {
        return IsisContext.getPersistenceSession();
    }


    protected AdapterManagerExtended getAdapterManager() {
        return getPersistenceSession().getAdapterManager();
    }


}
