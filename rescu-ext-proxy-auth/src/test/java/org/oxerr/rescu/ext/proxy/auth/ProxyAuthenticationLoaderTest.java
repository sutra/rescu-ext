package org.oxerr.rescu.ext.proxy.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

class ProxyAuthenticationLoaderTest {

	@Test
	void testLoadProxyAuthentication() {
		assertNull(System.getProperty("jdk.http.auth.tunneling.disabledSchemes"));
		new ProxyAuthenticationLoader().loadProxyAuthentication();
		assertEquals(StringUtils.EMPTY, System.getProperty("jdk.http.auth.tunneling.disabledSchemes"));
	}

}
