package com.growth.common.objects;

import java.util.HashMap;

import org.joda.time.DateTime;
import lombok.Data;

public @Data class RuleSet {
	private String _id;
	private Instrument instrument;
	private HashMap<String, Rule> rules;
	private DateTime updated;
	private DateTime created;
}
