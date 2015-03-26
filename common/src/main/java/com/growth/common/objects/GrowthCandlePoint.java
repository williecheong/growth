package com.growth.common.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oanda.fxtrade.api.CandlePoint;
import lombok.Data;

@Data
public class GrowthCandlePoint implements CandlePoint {

    @JsonProperty
    double close;
    @JsonProperty
    double max;
    @JsonProperty
    double min;
    @JsonProperty
    double open;
    @JsonProperty
    long timestamp;

    public GrowthCandlePoint (CandlePoint candlePoint) {
        // constructor from Oanda's object into ours

        close = candlePoint.getClose();
        max = candlePoint.getMax();
        min = candlePoint.getMin();
        open = candlePoint.getOpen();
        timestamp = candlePoint.getTimestamp();
    }


    // stupid Oanda programming hack

    @Override
    public Object clone() {
        try {
            throw new CloneNotSupportedException();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        // lol......
        return null;
    }
}
