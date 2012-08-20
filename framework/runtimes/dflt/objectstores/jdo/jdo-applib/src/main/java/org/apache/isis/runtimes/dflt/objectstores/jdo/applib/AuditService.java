package org.apache.isis.runtimes.dflt.objectstores.jdo.applib;

import java.util.List;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.Hidden;

public class AuditService extends AbstractFactoryAndRepository {
    
    public List<AuditEntry> list() {
        return allInstances(AuditEntry.class);
    }
    
    @Hidden
    public AuditEntry audit(String user, long currentTimestampEpoch, String objectType, String identifier, String preValue, String postValue) {
        AuditEntry auditEntry = newTransientInstance(AuditEntry.class);
        auditEntry.setTimestampEpoch(currentTimestampEpoch);
        auditEntry.setUser(user);
        auditEntry.setObjectType(objectType);
        auditEntry.setIdentifier(identifier);
        auditEntry.setPreValue(preValue);
        auditEntry.setPostValue(postValue);
        return persist(auditEntry);
    }


}
