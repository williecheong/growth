package com.growth.common.objects;

import java.util.HashMap;

import org.joda.time.DateTime;
import lombok.Data;

public @Data class Rule {
	private String _id;
	private Pattern pattern;
	private PriceAction prediction;
	private double confidence;
}
