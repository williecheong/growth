package com.growth.common.objects;

import java.util.HashMap;

import org.joda.time.DateTime;

public class Pattern {
	private String _id;
	private String pattern;
	private Instrument instrument;
	private HashMap<PriceAction, Integer> frequency;
	private DateTime updated;
	private DateTime created;
}

