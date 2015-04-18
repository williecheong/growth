package com.growth.common.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.BasicDBObject;
import com.oanda.fxtrade.api.CandlePoint;
import lombok.Data;

@Data
public class GrowthCandlePoint extends BasicDBObject implements CandlePoint {

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

    public GrowthCandlePoint(CandlePoint candlePoint) {
        // constructor from Oanda's object into ours

        close = candlePoint.getClose();
        max = candlePoint.getMax();
        min = candlePoint.getMin();
        open = candlePoint.getOpen();
        timestamp = candlePoint.getTimestamp();

        this.put("close", close);
        this.put("max", max);
        this.put("min", min);
        this.put("open", open);
        this.put("timestamp", timestamp);
    }


    // dummy constructor
    public GrowthCandlePoint() {
    }


    // stupid Oanda programming hack

    @Override
    public Object clone() {
        // todo: hopefully not this...
        try {
            throw new CloneNotSupportedException();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        // lol......
        return null;
    }

    @Override
    public String toString() {
        // todo: not this
        String json = "{";

        for (String key : keySet()) {
            json += "\"" + key + "\":\"" + this.get(key) + "\",";
        }

        json = json.substring(0,json.length()-1) + "}";

        return json;
    }
}
