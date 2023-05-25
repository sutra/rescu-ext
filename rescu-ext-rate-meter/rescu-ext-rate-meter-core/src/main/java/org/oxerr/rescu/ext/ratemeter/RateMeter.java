package org.oxerr.rescu.ext.ratemeter;

import java.time.temporal.TemporalUnit;
import java.util.Optional;

public interface RateMeter {

	void increment();

	Optional<Long> getRate(TemporalUnit unit);

}
