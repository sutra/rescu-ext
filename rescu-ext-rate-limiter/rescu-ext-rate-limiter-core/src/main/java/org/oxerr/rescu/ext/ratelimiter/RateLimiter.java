package org.oxerr.rescu.ext.ratelimiter;

public interface RateLimiter {

	/**
	 * Acquires a permit from this RateLimiter, blocking until one is available.
	 *
	 * <p>
	 * Acquires a permit, if one is available and returns immediately, reducing the
	 * number of available permits by one.
	 * </p>
	 */
	void acquire();

}
