package org.apache.isis.applib.bookmarks;

import java.io.Serializable;
import java.util.Iterator;

import com.google.common.base.Splitter;

/**
 * String representation of any persistent object managed by the framework.
 * 
 * <p>
 * Analogous to the <tt>RootOid</tt>.
 */
public class Bookmark implements Serializable {

    private static final char SEPARATOR = ':';

    private static final long serialVersionUID = 1L;
    
    private final String objectType;
    private final String identifier;

    /**
     * Round-trip with {@link #toString()} representation.
     */
    public Bookmark(String toString) {
        this(Splitter.on(SEPARATOR).split(toString).iterator());
    }

    private Bookmark(Iterator<String> split) {
        this(split.next(), split.next());
    }

    public Bookmark(String objectType, String identifier) {
        this.objectType = objectType;
        this.identifier = identifier;
    }
    
    public String getObjectType() {
        return objectType;
    }
    
    public String getIdentifier() {
        return identifier;
    }
    
    
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
        result = prime * result + ((objectType == null) ? 0 : objectType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Bookmark other = (Bookmark) obj;
        if (identifier == null) {
            if (other.identifier != null)
                return false;
        } else if (!identifier.equals(other.identifier))
            return false;
        if (objectType == null) {
            if (other.objectType != null)
                return false;
        } else if (!objectType.equals(other.objectType))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return objectType + SEPARATOR + identifier;
    }
}
