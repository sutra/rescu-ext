package org.oxerr.rescu.ext.ratemeter.redisson;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.Optional;

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

	public RedissonRateMeter(
		final RedissonClient redisson,
		final String keyPrefix,
		final Duration measureDuration
	) {
		final String meterName = String.format("%s:rateMeter", keyPrefix);
		final String propsName = String.format("%s:props", meterName);

		this.rateMeter = redisson.getAtomicLong(meterName);
		this.props = redisson.getMap(propsName);

		this.init(measureDuration);

		this.rateMeter.addListener(new ExpiredObjectListener() {

			@Override
			public void onExpired(String name) {
				log.trace("onExpired({})", name);

				if (name.equals(meterName) || name.equals(propsName)) {
					init(measureDuration);
				}
			}

		});
	}

	private void init(final Duration measureDuration) {
		final Instant now = Instant.now();
		final Instant expireDate = now.plus(measureDuration);

		this.props.expire(expireDate);
		this.rateMeter.expire(expireDate);

		this.props.put(CREATED, now);
	}

	@Override
	public void increment() {
		this.rateMeter.incrementAndGetAsync();
	}

	@Override
	public Optional<Long> getRate(TemporalUnit unit) {
		long count = this.rateMeter.get();

		Instant created = this.props.get(CREATED);
		if (created == null) {
			return Optional.empty();
		}

		Instant now = Instant.now();

		return Optional.of(getRate(count, created, now, unit));
	}

	static long getRate(long count, Instant s, Instant e, TemporalUnit unit) {
		Duration duration = Duration.between(s, e);
		return count / duration.get(unit);
	}

}
