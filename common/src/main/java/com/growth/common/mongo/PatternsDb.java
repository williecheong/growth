package com.growth.common.mongo;

import com.growth.common.objects.GrowthCandlePoint;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.oanda.fxtrade.api.CandlePoint;
import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.net.UnknownHostException;

@Data
public class PatternsDb extends MainDb {

    public final String collectionName = "candle_by_year";
    final protected DBCollection collection;

	public PatternsDb() throws UnknownHostException {
		if (!super.db.collectionExists(collectionName)) {
	        super.db.createCollection(collectionName, null);
	    }
		
		collection = super.db.getCollection(collectionName);
	}
	
//	public void create() {
//		BasicDBObject document = new BasicDBObject("name", "MongoDB")
//						        .append("type", "database")
//						        .append("count", 1)
//						        .append("info", new BasicDBObject("x", 203).append("y", 102));
//		collection.insert(document);
//	}

    public void addDataPoint(LocalDate date, Integer price) {
        BasicDBObject document = new BasicDBObject()
                .append("price", price)
                .append("date", date.toString());

        collection.insert(document);

        System.out.println("Adding data point.");
    }

    public void insertYearCandleStickData(DateTime yearDate, GrowthCandlePoint[] candlePoints) {

        int year = yearDate.getYear();

        BasicDBList dbList = new BasicDBList();
        for (CandlePoint c : candlePoints) {
            dbList.add(c.toString());
        }

        BasicDBObject document = new BasicDBObject()
                .append("year", year)
                .append("candlePoints", dbList);

        collection.insert(document);

        System.out.println("Inserting candlestick data for.");


    }
	
}
