package com.growth.common.objects;

import java.util.HashMap;

import org.joda.time.DateTime;
import lombok.Data;

public @Data class Decision {
	private String _id;
	private Trade trade;
	private Rule ruleResponsible;
	private Market openConditions;
	
	private Market closeConditions;
	private double netProfit;
	
	private DateTime updated;
	private DateTime created;
}
