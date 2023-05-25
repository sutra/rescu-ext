package org.oxerr.rescu.ext.ratemeter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import si.mazi.rescu.Interceptor;

public class RateMeterInterceptor implements Interceptor {

	private final RateMeter rateMeter;

	public RateMeterInterceptor(RateMeter rateMeter) {
		this.rateMeter = rateMeter;
	}

	@Override
	public Object aroundInvoke(InvocationHandler invocationHandler, Object proxy, Method method, Object[] args)
			throws Throwable {
		try {
			return invocationHandler.invoke(proxy, method, args);
		} finally {
			this.rateMeter.increment();
		}
	}

}
