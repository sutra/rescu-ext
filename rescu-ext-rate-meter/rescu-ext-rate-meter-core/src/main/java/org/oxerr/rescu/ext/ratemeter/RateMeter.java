package org.oxerr.rescu.ext.ratemeter;

import java.time.temporal.TemporalUnit;

public interface RateMeter {

	void increment();

	long getRate(TemporalUnit unit);

}
