package io.bootique.jdbc.instrumented;

import io.bootique.test.junit.BQModuleProviderChecker;
import org.junit.Test;

public class InstrumentedJdbcModuleProviderIT {

	@Test
	public void testPresentInJar() {
		BQModuleProviderChecker.testPresentInJar(InstrumentedJdbcModuleProvider.class);
	}
}