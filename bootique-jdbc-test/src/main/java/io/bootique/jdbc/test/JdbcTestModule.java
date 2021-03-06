package io.bootique.jdbc.test;

import com.google.inject.Binder;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.bootique.ConfigModule;
import io.bootique.config.ConfigurationFactory;
import io.bootique.jdbc.DataSourceFactory;
import io.bootique.jdbc.JdbcModule;
import io.bootique.jdbc.test.derby.DerbyListener;
import io.bootique.jdbc.test.runtime.DatabaseChannelFactory;
import io.bootique.jdbc.test.runtime.DatabaseChannelFactoryFactory;
import io.bootique.log.BootLogger;

/**
 * @since 0.12
 */
public class JdbcTestModule extends ConfigModule {

    @Override
    public void configure(Binder binder) {
        // for now we only support Derby...
        JdbcModule.extend(binder).initAllExtensions().addDataSourceListener(DerbyListener.class);
    }

    @Singleton
    @Provides
    DatabaseChannelFactory provideDatabaseChannelFactory(
            ConfigurationFactory configFactory,
            DataSourceFactory dataSourceFactory) {
        return configFactory.config(DatabaseChannelFactoryFactory.class, configPrefix).createFactory(dataSourceFactory);
    }

    @Singleton
    @Provides
    DerbyListener provideDerbyLifecycleListener(BootLogger bootLogger) {
        return new DerbyListener(bootLogger);
    }
}
