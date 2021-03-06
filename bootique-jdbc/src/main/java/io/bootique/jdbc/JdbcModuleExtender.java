package io.bootique.jdbc;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import io.bootique.ModuleExtender;
import io.bootique.jdbc.managed.ManagedDataSourceFactory;

/**
 * @since 0.25
 */
public class JdbcModuleExtender extends ModuleExtender<JdbcModuleExtender> {

    private static final Key<Class<? extends ManagedDataSourceFactory>> FACTORY_TYPE_KEY = Key
            .get(new TypeLiteral<Class<? extends ManagedDataSourceFactory>>() {
            });

    private Multibinder<DataSourceListener> listeners;
    private Multibinder<Class<? extends ManagedDataSourceFactory>> factoryTypes;

    public JdbcModuleExtender(Binder binder) {
        super(binder);
    }

    @Override
    public JdbcModuleExtender initAllExtensions() {
        contributeListeners();
        contributeFactoryTypes();
        return this;
    }

    public JdbcModuleExtender addFactoryType(Class<? extends ManagedDataSourceFactory> factoryType) {
        contributeFactoryTypes().addBinding().toInstance(factoryType);
        return this;
    }

    public JdbcModuleExtender addDataSourceListener(DataSourceListener listener) {
        contributeListeners().addBinding().toInstance(listener);
        return this;
    }

    public JdbcModuleExtender addDataSourceListener(Class<? extends DataSourceListener> listenerType) {
        contributeListeners().addBinding().to(listenerType);
        return this;
    }

    private Multibinder<DataSourceListener> contributeListeners() {
        return listeners != null ? listeners : (listeners = newSet(DataSourceListener.class));
    }

    private Multibinder<Class<? extends ManagedDataSourceFactory>> contributeFactoryTypes() {
        return factoryTypes != null ? factoryTypes : (factoryTypes = newSet(FACTORY_TYPE_KEY));
    }
}
