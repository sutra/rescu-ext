package org.oxerr.rescu.ext.ratemeter.redisson;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

import org.junit.jupiter.api.Test;

class RedissonRateMeterTest {

	@Test
	void testGetRateLongInstantInstantTemporalUnit() {
		Instant s = Instant.now();
		Instant e = s.plus(Duration.ofMinutes(1));

		TemporalUnit unit = ChronoUnit.SECONDS;

		long rate = RedissonRateMeter.getRate(600, s, e, unit);
		assertEquals(10, rate);
	}

}
