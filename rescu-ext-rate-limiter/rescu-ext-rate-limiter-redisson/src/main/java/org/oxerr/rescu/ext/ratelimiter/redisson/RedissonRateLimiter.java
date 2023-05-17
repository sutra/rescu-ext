package org.oxerr.rescu.ext.ratelimiter.redisson;

import org.oxerr.rescu.ext.ratelimiter.RateLimiter;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;

public class RedissonRateLimiter implements RateLimiter {

	private final RRateLimiter rateLimiter;

	public RedissonRateLimiter(String keyPrefix, RedissonClient redisson) {
		String limiterName = String.format("%s:rateLimiter", keyPrefix);
		this.rateLimiter = redisson.getRateLimiter(limiterName);
	}

	/**
	 * Updates RateLimiter's state and stores config to Redis server.
	 *
	 * @param mode             - rate mode
	 * @param rate             - rate
	 * @param rateInterval     - rate time interval
	 * @param rateIntervalUnit - rate time interval unit
	 */
	public void setRate(RateType mode, long rate, long rateInterval, RateIntervalUnit rateIntervalUnit) {
		this.rateLimiter.setRate(mode, rate, rateInterval, rateIntervalUnit);
	}

	@Override
	public void acquire() {
		this.rateLimiter.acquire();
	}

}
