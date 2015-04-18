package com.growth.common.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.growth.common.objects.GrowthCandlePoint;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

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

    public void insertYearCandleStickData(DateTime yearDate, List<GrowthCandlePoint> candlePoints) {

        int year = yearDate.getYear();

        System.out.println(candlePoints);


        BasicDBList dbList = new BasicDBList();
        dbList.addAll(candlePoints);


        BasicDBObject document = new BasicDBObject()
                .append("year", year)
                .append("candlePoints", dbList);

        collection.insert(document);
    }

    public List<GrowthCandlePoint> loadCandleStickDataForYear(DateTime yearDate) throws IOException {

        int year = yearDate.getYear();

        DBObject result = collection.findOne();

        List<GrowthCandlePoint> candlePoints = new ArrayList<>();

        String str = result.get("candlePoints").toString();
        ObjectMapper mapper = new ObjectMapper();
        GrowthCandlePoint[] candlePointsArray = mapper.readValue(str, GrowthCandlePoint[].class);

        for (GrowthCandlePoint growthCandlePoint : candlePointsArray) {
            candlePoints.add(growthCandlePoint);
        }

        return candlePoints;
    }
	
}
