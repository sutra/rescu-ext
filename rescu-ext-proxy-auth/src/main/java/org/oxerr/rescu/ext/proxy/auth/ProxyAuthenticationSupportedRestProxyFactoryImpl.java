package org.oxerr.rescu.ext.proxy.auth;

import si.mazi.rescu.ClientConfig;
import si.mazi.rescu.IRestProxyFactory;
import si.mazi.rescu.Interceptor;

public class ProxyAuthenticationSupportedRestProxyFactoryImpl implements IRestProxyFactory {

	private final IRestProxyFactory restProxyFactory;

	public ProxyAuthenticationSupportedRestProxyFactoryImpl(IRestProxyFactory restProxyFactory) {
		this.restProxyFactory = restProxyFactory;
		new ProxyAuthenticationLoader().loadProxyAuthentication();
	}

	@Override
	public <I> I createProxy(Class<I> restInterface, String baseUrl, ClientConfig config, Interceptor... interceptors) {
		return this.restProxyFactory.createProxy(restInterface, baseUrl, config, interceptors);
	}

	@Override
	public <I> I createProxy(Class<I> restInterface, String baseUrl) {
		return this.restProxyFactory.createProxy(restInterface, baseUrl);
	}

}
