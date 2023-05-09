package org.oxerr.rescu.ext.singleton;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

import si.mazi.rescu.RestProxyFactoryImpl;

class RestProxyFactorySingletonImplTest {

	@Test
	void testCreateProxyClassOfIStringClientConfigInterceptorArray() {
		RestProxyFactorySingletonImpl f = new RestProxyFactorySingletonImpl(new RestProxyFactoryImpl());
		assertSame(f.createProxy(SellerListingResource.class, null, null), f.createProxy(SellerListingResource.class, null, null));
	}

	@Test
	void testCreateProxyClassOfIString() {
		RestProxyFactorySingletonImpl f = new RestProxyFactorySingletonImpl(new RestProxyFactoryImpl());
		assertSame(f.createProxy(SellerListingResource.class, null), f.createProxy(SellerListingResource.class, null));
	}

}
