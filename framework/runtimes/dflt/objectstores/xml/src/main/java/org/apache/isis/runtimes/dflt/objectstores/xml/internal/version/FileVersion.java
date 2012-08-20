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

package org.apache.isis.runtimes.dflt.objectstores.xml.internal.version;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

import org.apache.isis.core.commons.encoding.DataInputExtended;
import org.apache.isis.core.commons.encoding.Encodable;
import org.apache.isis.core.commons.lang.ToString;
import org.apache.isis.core.metamodel.adapter.version.Version;
import org.apache.isis.core.metamodel.adapter.version.VersionUserAndTimeAbstract;
import org.apache.isis.runtimes.dflt.objectstores.xml.internal.clock.Clock;

public class FileVersion extends VersionUserAndTimeAbstract implements Encodable, Serializable {
    
    private static final long serialVersionUID = 1L;
    private static Clock clock;

    public static void setClock(final Clock clock) {
        FileVersion.clock = clock;
    }

    
    // ///////////////////////////////////////////////////////
    // constructor
    // ///////////////////////////////////////////////////////

    public FileVersion(final String user) {
        this(user, clock.getTime());
    }

    public FileVersion(final String user, final long sequence) {
        super(user, new Date(sequence));
    }

    public FileVersion(final DataInputExtended input) throws IOException {
        super(input);
    }


    // ///////////////////////////////////////////////////////
    //
    // ///////////////////////////////////////////////////////

    public long getSequence() {
        return getTime().getTime();
    }

    @Override
    public String sequence() {
        return Long.toString(getSequence(), 16);
    }

    @Override
    public boolean different(final Version version) {
        if (!(version instanceof FileVersion)) {
            return false;
        } 
        final FileVersion other = (FileVersion) version;
        return !sameTime(other);
    }

    private boolean sameTime(final FileVersion other) {
        return getTime().getTime() == other.getTime().getTime();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof FileVersion) {
            return sameTime((FileVersion) obj);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("sequence", getTime().getTime());
        str.append("time", getTime());
        str.append("user", getUser());
        return str.toString();
    }
}
