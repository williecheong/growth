package com.growth.common.objects;

import org.joda.time.DateTime;
import lombok.Data;

public @Data class Market {
	private Instrument instrument;
	private double exchangeRate;
	private double spread;
	private double volume;
	private DateTime timestamp;
}
