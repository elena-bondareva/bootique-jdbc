package io.bootique.jdbc.instrumented.hikaricp;

import com.codahale.metrics.MetricRegistry;
import com.zaxxer.hikari.HikariDataSource;
import io.bootique.BQRuntime;
import io.bootique.jdbc.DataSourceFactory;
import io.bootique.jdbc.instrumented.hikaricp.metrics.HikariMetricsBridge;
import io.bootique.test.junit.BQTestFactory;
import org.junit.Rule;
import org.junit.Test;

import javax.sql.DataSource;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class HikariCPMetricsInitializerIT {

    @Rule
    public BQTestFactory testFactory = new BQTestFactory();

    @Test
    public void testMetrics() {
        BQRuntime runtime = testFactory
                .app("-c", "classpath:io/bootique/jdbc/instrumented/hikaricp/HikariCPMetricsInitializer.yml")
                .autoLoadModules()
                .createRuntime();

        DataSourceFactory factory = runtime.getInstance(DataSourceFactory.class);

        DataSource ds = factory.forName("derby1");
        assertNotNull(ds);
        assertTrue(ds instanceof HikariDataSource);

        HikariDataSource hikariDS = (HikariDataSource) ds;
        String poolName = hikariDS.getPoolName();

        MetricRegistry metricRegistry = runtime.getInstance(MetricRegistry.class);
        assertEquals(1, metricRegistry.getTimers().size());
        assertEquals(MetricRegistry.name(HikariMetricsBridge.waitMetric(poolName)),
                metricRegistry.getTimers().firstKey());

        assertEquals(4, metricRegistry.getGauges().size());
        assertEquals(new HashSet<String>() {{
            add(HikariMetricsBridge.totalConnectionsMetric(poolName));
            add(HikariMetricsBridge.idleConnectionsMetric(poolName));
            add(HikariMetricsBridge.activeConnectionsMetric(poolName));
            add(HikariMetricsBridge.pendingConnectionsMetric(poolName));
        }}, metricRegistry.getGauges().keySet());
    }
}