package org.oxerr.rescu.ext.singleton;

import java.util.HashMap;
import java.util.Map;

import org.oxerr.rescu.ext.proxy.auth.ProxyAuthenticationSupportedRestProxyFactoryImpl;

import si.mazi.rescu.ClientConfig;
import si.mazi.rescu.IRestProxyFactory;
import si.mazi.rescu.Interceptor;

public class RestProxyFactorySingletonImpl implements IRestProxyFactory {

	private final IRestProxyFactory restProxyFactory;

	private final Map<Class<?>, Object> cache = new HashMap<>();

	public RestProxyFactorySingletonImpl(IRestProxyFactory restProxyFactory) {
		this.restProxyFactory = new ProxyAuthenticationSupportedRestProxyFactoryImpl(restProxyFactory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <I> I createProxy(Class<I> restInterface, String baseUrl, ClientConfig config, Interceptor... interceptors) {
		I i = (I) this.cache.get(restInterface);

		if (i == null) {
			i = this.restProxyFactory.createProxy(restInterface, baseUrl, config, interceptors);
			synchronized (this.cache) {
				I previous = (I) this.cache.putIfAbsent(restInterface, i);
				if (previous != null) {
					i = previous;
				}
			}
		}

		return i;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <I> I createProxy(Class<I> restInterface, String baseUrl) {
		I i = (I) this.cache.get(restInterface);

		if (i == null) {
			i = this.restProxyFactory.createProxy(restInterface, baseUrl);
			synchronized (this.cache) {
				I previous = (I) this.cache.putIfAbsent(restInterface, i);
				if (previous != null) {
					i = previous;
				}
			}
		}

		return i;
	}

}
