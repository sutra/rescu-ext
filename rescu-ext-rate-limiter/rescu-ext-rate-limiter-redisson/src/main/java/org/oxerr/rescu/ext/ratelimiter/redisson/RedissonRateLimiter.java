package org.oxerr.rescu.ext.ratelimiter.redisson;

import java.time.Duration;

import org.oxerr.rescu.ext.ratelimiter.RateLimiter;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;

public class RedissonRateLimiter implements RateLimiter {

	private final RRateLimiter rateLimiter;

	public RedissonRateLimiter(RRateLimiter rateLimiter) {
		this.rateLimiter = rateLimiter;
	}

	public RedissonRateLimiter(RedissonClient redisson, String name) {
		this.rateLimiter = redisson.getRateLimiter(name);
	}

	/**
	 * Updates RateLimiter's state and stores config to Redis server.
	 *
	 * @param mode			 - rate mode
	 * @param rate			 - rate
	 * @param rateInterval	 - rate time interval
	 * @param rateIntervalUnit - rate time interval unit
	 * @deprecated Use {@link #setRate(RateType, long, Duration)} instead.
	 */
	@Deprecated(forRemoval = true, since = "1.1.0")
	public void setRate(RateType mode, long rate, long rateInterval, RateIntervalUnit rateIntervalUnit) {
		this.rateLimiter.setRate(mode, rate, rateInterval, rateIntervalUnit);
	}

	/**
	 * Sets the rate limit and clears the state.
	 * Overrides both limit and state if they haven't been set before.
	 *
	 * @param mode rate mode
	 * @param rate rate
	 * @param rateInterval rate time interval
	 */
	public void setRate(RateType mode, long rate, Duration rateInterval) {
		this.rateLimiter.setRate(mode, rate, rateInterval);
	}

	@Override
	public void acquire() {
		this.rateLimiter.acquire();
	}

	public RRateLimiter getRateLimiter() {
		return this.rateLimiter;
	}

}
