package org.oxerr.rescu.ext.ratemeter.redisson;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;

class RedissonRateMeterTest {

	@Test
	void testGetRateLongInstantInstantTemporalUnit() {
		Instant s = Instant.now();
		Instant e = s.plus(Duration.ofMinutes(1));

		TemporalUnit unit = ChronoUnit.SECONDS;

		long rate = RedissonRateMeter.getRate(600, s, e, unit).get().longValue();
		assertEquals(10, rate);
	}

	public static void main(String[] args) throws InterruptedException {
		final RedissonClient redisson = Redisson.create();
		final String keyPrefix = "test";

		final RedissonRateMeter meter = new RedissonRateMeter(redisson, keyPrefix);

		for (int i = 0; i < 50; i++) {
			meter.increment();
		}

		System.out.println("Sleeping 5 seconds...");
		Thread.sleep(5000);

		final long rate = meter.getRate(ChronoUnit.SECONDS).get().longValue();
		System.out.printf("rate: %d\n", rate);

		System.out.println("Sleeping 15 seconds...");
		Thread.sleep(15 * 1000);

		System.out.println("Shutting down...");
		redisson.shutdown();
	}

}
