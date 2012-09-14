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
package org.apache.isis.runtimes.dflt.objectstores.sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import org.apache.isis.core.testsupport.files.Files;
import org.apache.isis.core.testsupport.files.Files.Recursion;
import org.apache.isis.runtimes.dflt.objectstores.sql.common.SqlIntegrationTestCommonBase;
import org.apache.isis.runtimes.dflt.objectstores.sql.common.SqlIntegrationTestFixtures;
import org.apache.isis.runtimes.dflt.objectstores.sql.common.SqlIntegrationTestFixtures.State;
import org.apache.isis.tck.dom.poly.Empty;
import org.apache.isis.tck.dom.poly.EmptyEntityWithOwnProperty;
import org.apache.isis.tck.dom.poly.ReferencingPolyTypesEntity;
import org.apache.isis.tck.dom.poly.SelfReferencingEntity;
import org.apache.isis.tck.dom.poly.StringBaseEntity;
import org.apache.isis.tck.dom.poly.StringBaseEntitySub;
import org.apache.isis.tck.dom.poly.StringBaseEntitySubThree;
import org.apache.isis.tck.dom.poly.StringBaseEntitySubTwo;
import org.apache.isis.tck.dom.poly.Stringable;
import org.apache.isis.tck.dom.poly.StringableEntityWithOwnDerivedProperty;
import org.apache.isis.tck.dom.poly.StringableEntityWithOwnProperties;
import org.apache.isis.tck.dom.poly.StringableEntityWithOwnProperty;

public class PolymorphismTest extends SqlIntegrationTestCommonBase {

    private static final String IMPL_B_STRING = "Impl B String";

    private static final String IMPL_A_STRING = "Impl A String";
    private static final String CHILD_1 = "Child 1";

    private static StringableEntityWithOwnProperty polyIntImpA;
    private static StringableEntityWithOwnProperties polyIntImpB;

    @Override
    public String getPropertiesFilename() {
        return "hsql-poly.properties";
    }

    @Override
    public void resetPersistenceStoreDirectlyIfRequired() {
        Files.deleteFilesWithPrefix("hsql-db", "poly", Recursion.DONT_RECURSE);
    }

    @Override
    public String getSqlTeardownString() {
        return "SHUTDOWN;";
    }

    @Test
    public void testSetupStore() throws Exception {
        setupFixtures();

        setUpFactory();

        create();
    }

    @Test
    public void testAll() throws Exception {
        load();

        setUpFactory();

        polymorphicLoad();
        interfaceLoad();
        loadSelfReferencingCollection();
        interfaceLoadProperty();
        interfaceLoadCollection();
        interfaceEditSave();
        interfaceEditLoad();
        allInterfacesInstancesLoaded();
        interfacesLoadedByQuery();
        interfacesLoadedByQuerySpecial();
        findByMatchPartialEntity();
        cannotFindByMatchWithWrongValue();
        reinitializeFixtures();
    }

    private void setupFixtures() {
        resetPersistenceStoreDirectlyIfRequired();
        setFixtureInitializationState(State.INITIALIZE);
    }

    private void create() throws Exception {
        getSqlIntegrationTestFixtures().dropTable("ISIS_SELFREFERENCINGENTITY");
        getSqlIntegrationTestFixtures().dropTable("ISIS_STRINGABLEENTITYWITHOWNPROPERTY");
        getSqlIntegrationTestFixtures().dropTable("ISIS_STRINGBASEENTITYSUB");
        getSqlIntegrationTestFixtures().dropTable("ISIS_STRINGBASEENTITYSUBTWO");
        getSqlIntegrationTestFixtures().dropTable("ISIS_STRINGBASEENTITYSUBTHREE");
        getSqlIntegrationTestFixtures().dropTable("ISIS_REFERENCINGPOLYTYPESENTITY");
        getSqlIntegrationTestFixtures().dropTable("ISIS_STRINGBASEENTITY");
        getSqlIntegrationTestFixtures().dropTable("ISIS_STRINGABLE");

        final ReferencingPolyTypesEntity referencingPolyTypesEntity = factory.newPolyTestClass();
        referencingPolyTypesEntity.setString("polyTestClassString");

        // Setup self-referencing collection
        final SelfReferencingEntity polySelfRefClassParent = factory.newPolySelfRefClass();
        polySelfRefClassParent.setString("Parent");

        final SelfReferencingEntity polySelfRefClassChild1 = factory.newPolySelfRefClass();
        polySelfRefClassChild1.setString(CHILD_1);
        polySelfRefClassParent.addToPolySelfRefClasses(polySelfRefClassChild1);

        final SelfReferencingEntity polySelfRefClassChild2 = factory.newPolySelfRefClass();
        polySelfRefClassChild2.setString("Child 2");
        polySelfRefClassParent.addToPolySelfRefClasses(polySelfRefClassChild2);
        factory.save(polySelfRefClassChild2);

        final SelfReferencingEntity polySelfRefClassChild3 = factory.newPolySelfRefClass();
        polySelfRefClassChild3.setString("Child 1 of Child 1");
        polySelfRefClassChild1.addToPolySelfRefClasses(polySelfRefClassChild3);

        factory.save(polySelfRefClassChild3);
        factory.save(polySelfRefClassChild1);

        factory.save(polySelfRefClassParent);
        referencingPolyTypesEntity.setPolySelfRefClass(polySelfRefClassParent);

        // polyTestClass.setPolyTestInterface(polyTestClass);

        polyIntImpA = factory.newPolyInterfaceImplA();
        polyIntImpA.setString(IMPL_A_STRING);
        polyIntImpA.setSpecial("special");
        factory.save(polyIntImpA);

        referencingPolyTypesEntity.setPolyInterfaceType(polyIntImpA);
        referencingPolyTypesEntity.getPolyInterfaces().add(polyIntImpA);

        // setup the polyTestClass
        final StringBaseEntitySub stringBaseEntitySub = factory.newPolySubClassOne();
        stringBaseEntitySub.setStringBase("PolySubClassOne 1");
        stringBaseEntitySub.setStringClassOne("Class 1");

        final StringBaseEntitySubTwo stringBaseEntitySubTwo = factory.newPolySubClassTwo();
        stringBaseEntitySubTwo.setStringBase("PolySubClassTwo 1");
        stringBaseEntitySubTwo.setStringClassTwo("Class 2");

        final StringBaseEntitySubThree stringBaseEntitySubThree = factory.newPolySubClassThree();
        stringBaseEntitySubThree.setStringBase("PolySubClassThree 1");
        stringBaseEntitySubThree.setStringClassThree("Another String");
        stringBaseEntitySubThree.setStringClassTwo("Class 3");

        referencingPolyTypesEntity.getPolyBaseClasses().add(stringBaseEntitySub);
        referencingPolyTypesEntity.getPolyBaseClasses().add(stringBaseEntitySubTwo);
        referencingPolyTypesEntity.getPolyBaseClasses().add(stringBaseEntitySubThree);

        factory.save(stringBaseEntitySub);
        factory.save(stringBaseEntitySubTwo);
        factory.save(stringBaseEntitySubThree);

        // store it and step the state engine
        factory.save(referencingPolyTypesEntity);

        setFixtureInitializationState(State.DONT_INITIALIZE, "in-memory");
    }

    private void load() {
        final List<ReferencingPolyTypesEntity> dataClasses = factory.allPolyTestClasses();
        assertEquals(1, dataClasses.size());
        final ReferencingPolyTypesEntity referencingPolyTypesEntity = dataClasses.get(0);

        getSqlIntegrationTestFixtures().setPolyTestClass(referencingPolyTypesEntity);

        setFixtureInitializationState(State.DONT_INITIALIZE);
    }

    private void polymorphicLoad() {
        final List<StringBaseEntity> polyBaseClasses = referencingPolyTypesEntity.getPolyBaseClasses();
        assertEquals(3, polyBaseClasses.size());

        StringBaseEntity polyClassBase = polyBaseClasses.get(0);
        assertTrue(polyClassBase instanceof StringBaseEntitySub);
        assertEquals("PolySubClassOne 1", polyClassBase.getStringBase());
        final StringBaseEntitySub stringBaseEntitySub = (StringBaseEntitySub) polyClassBase;
        assertEquals("Class 1", stringBaseEntitySub.getStringClassOne());

        polyClassBase = polyBaseClasses.get(1);
        assertTrue(polyClassBase instanceof StringBaseEntitySubTwo);
        final StringBaseEntitySubTwo stringBaseEntitySubTwo = (StringBaseEntitySubTwo) polyClassBase;
        assertEquals("Class 2", stringBaseEntitySubTwo.getStringClassTwo());

        polyClassBase = polyBaseClasses.get(2);
        assertTrue(polyClassBase instanceof StringBaseEntitySubThree);
        final StringBaseEntitySubThree stringBaseEntitySubThree = (StringBaseEntitySubThree) polyClassBase;
        assertEquals("Class 3", stringBaseEntitySubThree.getStringClassTwo());
        assertEquals("Another String", stringBaseEntitySubThree.getStringClassThree());
    }

    private void interfaceLoad() {
        final Stringable loaded = referencingPolyTypesEntity.getPolyInterfaceType();
        factory.resolve(loaded);
        assertEquals(polyIntImpA.getString(), loaded.getString());
    }

    private void loadSelfReferencingCollection() {
        final SelfReferencingEntity polySelfRefParent = referencingPolyTypesEntity.getPolySelfRefClass();
        final List<SelfReferencingEntity> list = polySelfRefParent.getPolySelfRefClasses();
        assertEquals(2, list.size());

        SelfReferencingEntity polySelfRefChild1 = null;
        for (final SelfReferencingEntity selfReferencingEntity : list) {
            if (selfReferencingEntity.getString().equals(CHILD_1)) {
                polySelfRefChild1 = selfReferencingEntity;
            }
        }
        assertNotNull(polySelfRefChild1);

        assertEquals(CHILD_1, polySelfRefChild1.title());

        List<SelfReferencingEntity> list2 = polySelfRefChild1.getPolySelfRefClasses();
        factory.resolve(polySelfRefChild1);
        list2 = polySelfRefChild1.getPolySelfRefClasses();
        assertEquals(1, list2.size());
    }

    private void interfaceLoadProperty() {
        final Stringable loaded = referencingPolyTypesEntity.getPolyInterfaceType();
        assertEquals(polyIntImpA.getString(), loaded.getString());
    }

    private void interfaceLoadCollection() {
        final List<Stringable> list = referencingPolyTypesEntity.getPolyInterfaces();

        assertEquals(1, list.size());
        final Stringable loaded = list.get(0);

        assertEquals(polyIntImpA.getString(), loaded.getString());
    }

    private void interfaceEditSave() {
        polyIntImpB = factory.newPolyInterfaceImplB();
        polyIntImpB.setString(IMPL_B_STRING);
        polyIntImpB.setSpecial("special");
        polyIntImpB.setInteger(1);

        factory.save(polyIntImpB);

        referencingPolyTypesEntity.setPolyInterfaceType(polyIntImpB);

        setFixtureInitializationState(State.INITIALIZE);
    }

    private void interfaceEditLoad() {
        load(); // reload data

        final Stringable loaded = referencingPolyTypesEntity.getPolyInterfaceType();
        assertEquals(polyIntImpB.getString(), loaded.getString());
    }

    private void allInterfacesInstancesLoaded() {
        final List<Stringable> list = factory.allPolyInterfaces();
        assertEquals(2, list.size());
    }

    private void interfacesLoadedByQuery() {
        // PolyInterface query = polyIntImpA;

        final StringableEntityWithOwnDerivedProperty query = new StringableEntityWithOwnDerivedProperty();
        query.setString(IMPL_A_STRING);

        final List<Stringable> list = factory.queryPolyInterfaces(query);
        assertEquals(1, list.size());
    }

    private void interfacesLoadedByQuerySpecial() {

        final StringableEntityWithOwnDerivedProperty query = new StringableEntityWithOwnDerivedProperty();

        final List<Stringable> list = factory.queryPolyInterfaces(query);
        assertEquals(2, list.size());
    }

    private void findByMatchPartialEntity() {
        final Empty match = new EmptyEntityWithOwnProperty();
        final List<Empty> matches = factory.allEmptyInterfacesThatMatch(match);
        assertEquals(1, matches.size());

        final Empty empty = matches.get(0);
        final StringableEntityWithOwnProperties imp = (StringableEntityWithOwnProperties) empty;
        assertEquals(IMPL_B_STRING, imp.getString());
    }

    private void cannotFindByMatchWithWrongValue() {
        final StringableEntityWithOwnProperties match = new StringableEntityWithOwnProperties();
        match.setInteger(0);
        final List<Empty> matches = factory.allEmptyInterfacesThatMatch(match);
        assertEquals(0, matches.size());
    }

    private void reinitializeFixtures() {
        setFixtureInitializationState(State.INITIALIZE);
        SqlIntegrationTestFixtures.recreate();
    }

}
