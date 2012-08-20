package org.apache.isis.core.metamodel.adapter.oid;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.apache.isis.core.metamodel.spec.ObjectSpecId;
import org.junit.Test;

public class RootOidDefaultTest_compareAgainst  {

    private ObjectSpecId cusObjectSpecId = ObjectSpecId.of("CUS");
    private ObjectSpecId ordObjectSpecId = ObjectSpecId.of("ORD");
    
    private RootOid oid1, oid2;
    
    @Test
    public void whenEquivalentAndSameVersion() throws Exception {
        oid1 = RootOidDefault.create(cusObjectSpecId, "123", 90807L);
        oid2 = RootOidDefault.create(cusObjectSpecId, "123", 90807L);
        
        assertThat(oid1, is(equalTo(oid2)));
        assertThat(oid1.compareAgainst(oid2), is(RootOid.Comparison.EQUIVALENT_AND_UNCHANGED));
    }
    
    @Test
    public void whenEquivalentAndDifferentVersions() throws Exception {
        oid1 = RootOidDefault.create(cusObjectSpecId, "123", 90807L);
        oid2 = RootOidDefault.create(cusObjectSpecId, "123", 90808L);
        
        assertThat(oid1, is(equalTo(oid2)));
        assertThat(oid1.compareAgainst(oid2), is(RootOid.Comparison.EQUIVALENT_BUT_CHANGED));
    }

    @Test
    public void whenEquivalentAndNoVersionInfoForLeftHand() throws Exception {
        oid1 = RootOidDefault.create(cusObjectSpecId, "123");
        oid2 = RootOidDefault.create(cusObjectSpecId, "123", 90808L);
        
        assertThat(oid1, is(equalTo(oid2)));
        assertThat(oid1.compareAgainst(oid2), is(RootOid.Comparison.EQUIVALENT_BUT_NO_VERSION_INFO));
    }

    @Test
    public void whenEquivalentAndNoVersionInfoForRightHand() throws Exception {
        oid1 = RootOidDefault.create(cusObjectSpecId, "123", 90807L);
        oid2 = RootOidDefault.create(cusObjectSpecId, "123");
        
        assertThat(oid1, is(equalTo(oid2)));
        assertThat(oid1.compareAgainst(oid2), is(RootOid.Comparison.EQUIVALENT_BUT_NO_VERSION_INFO));
    }

    @Test
    public void whenEquivalentAndNoVersionInfoForEither() throws Exception {
        oid1 = RootOidDefault.create(cusObjectSpecId, "123");
        oid2 = RootOidDefault.create(cusObjectSpecId, "123");
        
        assertThat(oid1, is(equalTo(oid2)));
        assertThat(oid1.compareAgainst(oid2), is(RootOid.Comparison.EQUIVALENT_BUT_NO_VERSION_INFO));
    }

    @Test
    public void whenNotEquivalent() throws Exception {
        oid1 = RootOidDefault.create(cusObjectSpecId, "123");
        oid2 = RootOidDefault.create(cusObjectSpecId, "124");
        
        assertThat(oid1, is(not(equalTo(oid2))));
        assertThat(oid1.compareAgainst(oid2), is(RootOid.Comparison.NOT_EQUIVALENT));
    }


}
