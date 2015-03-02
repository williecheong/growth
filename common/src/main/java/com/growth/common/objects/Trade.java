package com.growth.common.objects;

import lombok.Data;

public @Data class Trade {
	private String ticketId;
	private Instrument instrument;
	private Position position;
	private double quantity;
	private double takeProfit;
	private double stopLoss;
}
