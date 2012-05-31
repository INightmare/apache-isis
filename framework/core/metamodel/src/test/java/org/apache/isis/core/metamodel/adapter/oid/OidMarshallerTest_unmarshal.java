package org.apache.isis.core.metamodel.adapter.oid;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import org.apache.isis.core.metamodel.spec.ObjectSpecId;

/**
* <dt>CUS:123</dt>
* <dd>persistent root</dd>
* <dt>!CUS:123</dt>
* <dd>transient root</dd>
* <dt>CUS:123#items</dt>
* <dd>collection of persistent root</dd>
* <dt>!CUS:123#items</dt>
* <dd>collection of transient root</dd>
* <dt>CUS:123#NME:2</dt>
* <dd>aggregated object within persistent root</dd>
* <dt>!CUS:123~NME:2</dt>
* <dd>aggregated object within transient root</dd>
* <dt>CUS:123~NME:2~CTY:LON</dt>
* <dd>aggregated object within aggregated object within root</dd>
* <dt>CUS:123~NME:2#items</dt>
* <dd>collection of an aggregated object within root</dd>
* <dt>CUS:123~NME:2~CTY:LON#streets</dt>
* <dd>collection of an aggregated object within aggregated object within root</dd>
*/
public class OidMarshallerTest_unmarshal {

    private OidMarshaller oidMarshaller;
    
    @Before
    public void setUp() throws Exception {
        oidMarshaller = new OidMarshaller();
    }
    
    @Test
    public void persistentRoot() {
        final String oidStr = "CUS:123";
        
        final RootOidDefault rootOid = oidMarshaller.unmarshal(oidStr, RootOidDefault.class);
        assertThat(rootOid.isTransient(), is(false));
        assertThat(rootOid.getObjectSpecId(), is(ObjectSpecId.of("CUS")));
        assertThat(rootOid.getIdentifier(), is("123"));
        
        final Oid oid = oidMarshaller.unmarshal(oidStr, Oid.class);
        assertThat(oid, equalTo((Oid)rootOid));
    }

    @Test
    public void persistentRootWithFullyQualifiedSpecId() {
        final String oidStr = "com.planchase.ClassName:8";
        
        final RootOidDefault rootOid = oidMarshaller.unmarshal(oidStr, RootOidDefault.class);
        assertThat(rootOid.isTransient(), is(false));
        assertThat(rootOid.getObjectSpecId(), is(ObjectSpecId.of("com.planchase.ClassName")));
        assertThat(rootOid.getIdentifier(), is("8"));
        
        final Oid oid = oidMarshaller.unmarshal(oidStr, Oid.class);
        assertThat(oid, equalTo((Oid)rootOid));
    }

    @Test
    public void transientRoot() {
        final String oidStr = "!CUS:123";
        
        final RootOidDefault rootOid = oidMarshaller.unmarshal(oidStr, RootOidDefault.class);
        assertThat(rootOid.isTransient(), is(true));
        assertThat(rootOid.getObjectSpecId(), is(ObjectSpecId.of("CUS")));
        assertThat(rootOid.getIdentifier(), is("123"));
        
        final Oid oid = oidMarshaller.unmarshal(oidStr, Oid.class);
        assertThat(oid, equalTo((Oid)rootOid));
    }
    
    @Test
    public void collectionOfPersistentRoot() {
        final String oidStr = "CUS:123$items";
        
        final CollectionOid collectionOid = oidMarshaller.unmarshal(oidStr, CollectionOid.class);
        assertThat(collectionOid.isTransient(), is(false));
        assertThat(collectionOid.getParentOid(), is((TypedOid)oidMarshaller.unmarshal("CUS:123", RootOidDefault.class)));
        assertThat(collectionOid.getName(), is("items"));
        
        final Oid oid = oidMarshaller.unmarshal(oidStr, Oid.class);
        assertThat(oid, equalTo((Oid)collectionOid));
    }

    @Test
    public void collectionOfTransientRoot() {
        final String oidStr = "!CUS:123$items";
        
        final CollectionOid collectionOid = oidMarshaller.unmarshal(oidStr, CollectionOid.class);
        assertThat(collectionOid.isTransient(), is(true));
        assertThat(collectionOid.getParentOid(), is((TypedOid)oidMarshaller.unmarshal("!CUS:123", RootOidDefault.class)));
        assertThat(collectionOid.getName(), is("items"));
        
        final Oid oid = oidMarshaller.unmarshal(oidStr, Oid.class);
        assertThat(oid, equalTo((Oid)collectionOid));
    }

    @Test
    public void aggregatedWithinPersistent() {
        final String oidStr = "CUS:123~NME:2";
        
        final AggregatedOid aggregatedOid = oidMarshaller.unmarshal(oidStr, AggregatedOid.class);
        assertThat(aggregatedOid.isTransient(), is(false));
        assertThat(aggregatedOid.getParentOid(), is((TypedOid)oidMarshaller.unmarshal("CUS:123", RootOidDefault.class)));
        assertThat(aggregatedOid.getObjectSpecId(), is(ObjectSpecId.of("NME")));
        assertThat(aggregatedOid.getLocalId(), is("2"));
        
        final Oid oid = oidMarshaller.unmarshal(oidStr, Oid.class);
        assertThat(oid, equalTo((Oid)aggregatedOid));
    }

    @Test
    public void aggregatedWithinTransient() {
        final String oidStr = "!CUS:123~NME:2";
        
        final AggregatedOid aggregatedOid = oidMarshaller.unmarshal(oidStr, AggregatedOid.class);
        assertThat(aggregatedOid.isTransient(), is(true));
        assertThat(aggregatedOid.getParentOid(), is((TypedOid)oidMarshaller.unmarshal("!CUS:123", RootOidDefault.class)));
        assertThat(aggregatedOid.getObjectSpecId(), is(ObjectSpecId.of("NME")));
        assertThat(aggregatedOid.getLocalId(), is("2"));
        
        final Oid oid = oidMarshaller.unmarshal(oidStr, Oid.class);
        assertThat(oid, equalTo((Oid)aggregatedOid));
    }

    @Test
    public void aggregatedWithinAggregatedWithinRoot() {
        final String oidStr = "CUS:123~ADR:2~CTY:LON";
        
        final AggregatedOid aggregatedOid = oidMarshaller.unmarshal(oidStr, AggregatedOid.class);
        assertThat(aggregatedOid.isTransient(), is(false));
        assertThat(aggregatedOid.getParentOid(), is((TypedOid)oidMarshaller.unmarshal("CUS:123~ADR:2", AggregatedOid.class)));
        assertThat(aggregatedOid.getObjectSpecId(), is(ObjectSpecId.of("CTY")));
        assertThat(aggregatedOid.getLocalId(), is("LON"));
        
        final Oid oid = oidMarshaller.unmarshal(oidStr, Oid.class);
        assertThat(oid, equalTo((Oid)aggregatedOid));
    }

    @Test
    public void collectionOfAggregatedWithinRoot() {
        final String oidStr = "CUS:123~NME:2$items";
        
        final CollectionOid collectionOid = oidMarshaller.unmarshal(oidStr, CollectionOid.class);
        assertThat(collectionOid.isTransient(), is(false));
        assertThat(collectionOid.getParentOid(), is((TypedOid)oidMarshaller.unmarshal("CUS:123~NME:2", AggregatedOid.class)));
        assertThat(collectionOid.getName(), is("items"));
        
        final Oid oid = oidMarshaller.unmarshal(oidStr, Oid.class);
        assertThat(oid, equalTo((Oid)collectionOid));
    }

    @Test
    public void collectionOfAggregatedWithinAggregatedWithinRoot() {
        final String oidStr = "CUS:123~ADR:2~CTY:LON$streets";
        
        final CollectionOid collectionOid = oidMarshaller.unmarshal(oidStr, CollectionOid.class);
        assertThat(collectionOid.isTransient(), is(false));
        assertThat(collectionOid.getParentOid(), is((TypedOid)oidMarshaller.unmarshal("CUS:123~ADR:2~CTY:LON", AggregatedOid.class)));
        assertThat(collectionOid.getName(), is("streets"));
        
        final Oid oid = oidMarshaller.unmarshal(oidStr, Oid.class);
        assertThat(oid, equalTo((Oid)collectionOid));
    }
    

    @Test(expected=IllegalArgumentException.class)
    public void root_forCollection_oidStr() {
        oidMarshaller.unmarshal("CUS:123~NME:123$items", RootOidDefault.class);
    }

    @Test(expected=IllegalArgumentException.class)
    public void root_forAggregated_oidStr() {
        oidMarshaller.unmarshal("CUS:123~NME:123", RootOidDefault.class);
    }

    @Test(expected=IllegalArgumentException.class)
    public void collection_forRoot_oidStr() {
        oidMarshaller.unmarshal("CUS:123", CollectionOid.class);
    }

    @Test(expected=IllegalArgumentException.class)
    public void collection_forAggregated_oidStr() {
        oidMarshaller.unmarshal("CUS:123~NME:123", CollectionOid.class);
    }

    
    @Test(expected=IllegalArgumentException.class)
    public void aggregated_forPersistentRoot_oidStr() {
        oidMarshaller.unmarshal("CUS:123", AggregatedOid.class);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void aggregated_forTransientRoot_oidStr() {
        oidMarshaller.unmarshal("!CUS:123", AggregatedOid.class);
    }

    @Test(expected=IllegalArgumentException.class)
    public void aggregated_forCollection_oidStr() {
        oidMarshaller.unmarshal("CUS:123~NME:123$items", AggregatedOid.class);
    }

    @Test(expected=IllegalArgumentException.class)
    public void badPattern() {
        oidMarshaller.unmarshal("xxx", RootOidDefault.class);
    }

    

}
