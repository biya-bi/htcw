/**
 * 
 */
package org.rainbow.catalina.config;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.rainbow.catalina.Service;
import org.rainbow.catalina.core.Constants;
import org.rainbow.catalina.core.SimpleServer;
import org.rainbow.catalina.util.StringManager;

/**
 * @author biya-bi
 *
 */
public class SimpleServerTest {
	private static StringManager sm = StringManager.getManager(Constants.PACKAGE);

	private SimpleServer simpleServer = new SimpleServer();

	@Test
	public void addService_NoServicesExist_ServicesAdded() {
		Service service1 = Mockito.mock(Service.class);
		Service service2 = Mockito.mock(Service.class);

		Mockito.when(service1.getName()).thenReturn("service1");
		Mockito.when(service2.getName()).thenReturn("service2");

		simpleServer.addService(service1);
		simpleServer.addService(service2);

		Service[] expecteds = { service1, service2 };
		Arrays.sort(expecteds, (s1, s2) -> s1.getName().compareTo(s2.getName()));

		Service[] actuals = simpleServer.findServices();
		Assert.assertArrayEquals(expecteds, actuals);
	}

	@Test(expected = NullPointerException.class)
	public void addService_NullIsAdded_NullPointerExceptionThrown() {
		try {
			simpleServer.addService(null);
		} catch (NullPointerException e) {
			Assert.assertEquals(sm.getString("simpleServer.serviceArgumentCannotBeNull"), e.getMessage());
			throw e;
		}
	}

	@Test
	public void findService_ServiceExists_ServiceReturned() {
		Service service1 = Mockito.mock(Service.class);
		Service service2 = Mockito.mock(Service.class);

		final String service1Name = "service1";
		final String service2Name = "service2";

		Mockito.when(service1.getName()).thenReturn(service1Name);
		Mockito.when(service2.getName()).thenReturn(service2Name);

		simpleServer.addService(service1);
		simpleServer.addService(service2);

		Service service = simpleServer.findService(service1Name);

		Assert.assertEquals(service1Name, service.getName());
	}

	@Test
	public void removeService_ServiceExists_ServiceRemoved() {
		Service service1 = Mockito.mock(Service.class);
		Service service2 = Mockito.mock(Service.class);

		final String service1Name = "service1";
		final String service2Name = "service2";

		Mockito.when(service1.getName()).thenReturn(service1Name);
		Mockito.when(service2.getName()).thenReturn(service2Name);

		simpleServer.addService(service1);
		simpleServer.addService(service2);

		Mockito.when(service1.getName()).thenReturn(service1Name);

		simpleServer.removeService(service1);

		Service[] expecteds = { service2 };
		Service[] actuals = simpleServer.findServices();

		Assert.assertArrayEquals(expecteds, actuals);
	}
}
