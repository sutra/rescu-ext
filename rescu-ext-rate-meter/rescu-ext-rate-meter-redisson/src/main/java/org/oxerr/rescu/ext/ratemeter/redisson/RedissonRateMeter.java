package org.oxerr.rescu.ext.ratemeter.redisson;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.Optional;

import org.oxerr.rescu.ext.ratemeter.RateMeter;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

public class RedissonRateMeter implements RateMeter {

	private static final String CREATED = "created";

	private final RAtomicLong rateMeter;

	private final RMap<String, Instant> props;

	public RedissonRateMeter(
		final RedissonClient redisson,
		final String keyPrefix
	) {
		final String meterName = String.format("%s:rateMeter", keyPrefix);
		final String propsName = String.format("%s:props", meterName);

		this.rateMeter = redisson.getAtomicLong(meterName);
		this.props = redisson.getMap(propsName);

		this.init();
	}

	private void init() {
		final Instant now = Instant.now();

		this.rateMeter.set(0);
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

		return getRate(count, created, now, unit);
	}

	static Optional<Long> getRate(long count, Instant s, Instant e, TemporalUnit unit) {
		Duration duration = Duration.between(s, e);

		long u = duration.get(unit);
		if (u == 0) {
			return Optional.empty();
		}

		return Optional.of(count / u);
	}

}
