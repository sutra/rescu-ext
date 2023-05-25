package org.oxerr.rescu.ext.ratemeter.redisson;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalUnit;

import org.oxerr.rescu.ext.ratemeter.RateMeter;
import org.redisson.api.ExpiredObjectListener;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedissonRateMeter implements RateMeter {

	private static final String CREATED = "created";

	private final Logger log = LoggerFactory.getLogger(RedissonRateMeter.class);

	private final RAtomicLong rateMeter;

	private final RMap<String, Instant> props;

	public RedissonRateMeter(RedissonClient redisson, String keyPrefix, Duration measureDuration) {
		String meterName = String.format("%s:rateMeter", keyPrefix);
		String propsName = String.format("%s:props", meterName);

		Instant now = Instant.now();
		Instant expireDate = Instant.now().plus(measureDuration);

		this.rateMeter = redisson.getAtomicLong(meterName);
		this.rateMeter.expire(expireDate);

		this.props = redisson.getMap(propsName);
		this.props.expire(expireDate);

		this.props.put(CREATED, now);

		this.rateMeter.addListener(new ExpiredObjectListener() {

			@Override
			public void onExpired(String name) {
				log.trace("onExpired({})", name);

				if (name.equals(meterName)) {
					props.put(CREATED, Instant.now());
				}
			}

		});
	}

	@Override
	public void increment() {
		this.rateMeter.incrementAndGetAsync();
	}

	@Override
	public long getRate(TemporalUnit unit) {
		long count = this.rateMeter.get();

		Instant created = this.props.get(CREATED);
		Instant now = Instant.now();

		return getRate(count, created, now, unit);
	}

	static long getRate(long count, Instant s, Instant e, TemporalUnit unit) {
		Duration duration = Duration.between(s, e);
		return count / duration.get(unit);
	}

}
