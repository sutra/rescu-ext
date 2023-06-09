package org.oxerr.rescu.ext.ratelimiter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import si.mazi.rescu.Interceptor;

public class RateLimiterInterceptor implements Interceptor {

	private final RateLimiter rateLimiter;

	public RateLimiterInterceptor(RateLimiter rateLimiter) {
		this.rateLimiter = rateLimiter;
	}

	@Override
	public Object aroundInvoke(InvocationHandler invocationHandler, Object proxy, Method method, Object[] args)
			throws Throwable {
		this.rateLimiter.acquire();
		return invocationHandler.invoke(proxy, method, args);
	}

}
