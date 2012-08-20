package org.apache.isis.runtimes.dflt.objectstores.jdo.datanucleus;


import java.util.Map;

import org.apache.isis.core.commons.components.Installer;
import org.apache.isis.core.commons.config.IsisConfiguration;
import org.apache.isis.core.metamodel.adapter.ObjectAdapterFactory;
import org.apache.isis.core.metamodel.spec.SpecificationLoaderSpi;
import org.apache.isis.runtimes.dflt.bytecode.identity.objectfactory.ObjectFactoryBasic;
import org.apache.isis.runtimes.dflt.objectstores.jdo.datanucleus.persistence.adaptermanager.DataNucleusPojoRecreator;
import org.apache.isis.runtimes.dflt.objectstores.jdo.datanucleus.persistence.spi.DataNucleusIdentifierGenerator;
import org.apache.isis.runtimes.dflt.objectstores.jdo.datanucleus.persistence.spi.DataNucleusSimplePersistAlgorithm;
import org.apache.isis.runtimes.dflt.objectstores.jdo.datanucleus.persistence.spi.DataNucleusTransactionManager;
import org.apache.isis.runtimes.dflt.runtime.installerregistry.installerapi.PersistenceMechanismInstallerAbstract;
import org.apache.isis.runtimes.dflt.runtime.persistence.objectstore.ObjectStoreSpi;
import org.apache.isis.runtimes.dflt.runtime.persistence.objectstore.algorithm.PersistAlgorithm;
import org.apache.isis.runtimes.dflt.runtime.persistence.objectstore.transaction.TransactionalResource;
import org.apache.isis.runtimes.dflt.runtime.system.context.IsisContext;
import org.apache.isis.runtimes.dflt.runtime.system.persistence.AdapterManagerSpi;
import org.apache.isis.runtimes.dflt.runtime.system.persistence.IdentifierGenerator;
import org.apache.isis.runtimes.dflt.runtime.system.persistence.ObjectFactory;
import org.apache.isis.runtimes.dflt.runtime.system.transaction.EnlistedObjectDirtying;
import org.apache.isis.runtimes.dflt.runtime.system.transaction.IsisTransactionManager;

/**
 * Configuration files are read in the usual fashion (as per {@link Installer#getConfigurationResources()}, ie will consult all of:
 * <ul>
 * <li><tt>persistor_datanucleus.properties</tt>
 * <li><tt>persistor.properties</tt>
 * <li><tt>isis.properties</tt>
 * </ul>
 * 
 * <p>
 * With respect to configuration, all properties under {@value #ISIS_CONFIG_PREFIX} prefix are passed 
 * through verbatim to the DataNucleus runtime. For example:
 * <table>
 * <tr><th>Isis Property</th><th>DataNucleus Property</th></tr>
 * <tr><td><tt>isis.persistor.datanucleus.impl.datanucleus.foo.Bar</tt></td><td><tt>datanucleus.foo.Bar</tt></td></tr>
 * </table>
 *
 */
public class DataNucleusPersistenceMechanismInstaller extends PersistenceMechanismInstallerAbstract {

    public static final String NAME = "datanucleus";
    private static final String ISIS_CONFIG_PREFIX = "isis.persistor.datanucleus.impl";

    private DataNucleusApplicationComponents applicationComponents = null;
    
    public DataNucleusPersistenceMechanismInstaller() {
        super(NAME);
    }

    @Override
    protected ObjectStoreSpi createObjectStore(IsisConfiguration configuration, ObjectAdapterFactory adapterFactory, AdapterManagerSpi adapterManager) {
        createDataNucleusApplicationComponentsIfRequired(configuration);
        return new DataNucleusObjectStore(adapterFactory, applicationComponents);
    }

    private void createDataNucleusApplicationComponentsIfRequired(IsisConfiguration configuration) {
        if(applicationComponents != null) {
            return;
        }
        
        final IsisConfiguration dataNucleusConfig = configuration.createSubset(ISIS_CONFIG_PREFIX);
        final Map<String, String> props = dataNucleusConfig.asMap();
        
        applicationComponents = new DataNucleusApplicationComponents(props, getSpecificationLoader().allSpecifications());
    }

    @Override
    protected IdentifierGenerator createIdentifierGenerator(IsisConfiguration configuration) {
        return new DataNucleusIdentifierGenerator();
    }

    @Override
    protected IsisTransactionManager createTransactionManager(final EnlistedObjectDirtying persistor, final TransactionalResource objectStore) {
        return new DataNucleusTransactionManager(persistor, objectStore);
    }

    @Override
    protected PersistAlgorithm createPersistAlgorithm(IsisConfiguration configuration) {
        return new DataNucleusSimplePersistAlgorithm();
    }
    
    @Override
    protected ObjectFactory createObjectFactory(IsisConfiguration configuration) {
        return new ObjectFactoryBasic();
    }

    @Override
    protected DataNucleusPojoRecreator createPojoRecreator(IsisConfiguration configuration) {
        return new DataNucleusPojoRecreator();
    }
    
    protected SpecificationLoaderSpi getSpecificationLoader() {
        return IsisContext.getSpecificationLoader();
    }

}
