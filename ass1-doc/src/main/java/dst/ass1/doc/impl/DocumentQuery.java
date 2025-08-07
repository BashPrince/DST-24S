package dst.ass1.doc.impl;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.geojson.Polygon;
import com.mongodb.client.model.geojson.Position;
import dst.ass1.doc.IDocumentQuery;
import dst.ass1.jpa.util.Constants;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DocumentQuery implements IDocumentQuery {
    @Override
    public List<Document> findDocumentsByType(String type) {
        Bson filter = Filters.eq("type", type);

        try (MongoClient mongoClient = MongoClients.create()) {
            MongoDatabase database = mongoClient.getDatabase(Constants.MONGO_DB_NAME);
            MongoCollection<Document> collection = database.getCollection(Constants.COLL_LOCATION_DATA);

            return StreamSupport.stream(collection.find(filter).spliterator(), false)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<Document> findDocumentsByNameWithinPolygon(String name, List<List<Double>> polygon) {
        List<Position> positions = polygon.stream().map(p -> new Position(p.get(0), p.get(1))).collect(Collectors.toList());
        Polygon poly = new Polygon(positions);
        Bson filter = Filters.and(
                Filters.regex("name", name),
                Filters.geoWithin("geo", poly)
        );

        try (MongoClient mongoClient = MongoClients.create()) {
            MongoDatabase database = mongoClient.getDatabase(Constants.MONGO_DB_NAME);
            MongoCollection<Document> collection = database.getCollection(Constants.COLL_LOCATION_DATA);

            return StreamSupport.stream(collection.find(filter).spliterator(), false)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<Document> getAverageOpeningHoursPerCategory() {
        try (MongoClient mongoClient = MongoClients.create()) {
            MongoDatabase database = mongoClient.getDatabase(Constants.MONGO_DB_NAME);
            MongoCollection<Document> collection = database.getCollection(Constants.COLL_LOCATION_DATA);

            return StreamSupport.stream(
                    collection.aggregate(
                            Arrays.asList(
                                    Aggregates.match(Filters.exists("category")),
                                    Aggregates.project(
                                            Projections.fields(
                                                    Projections.include("category"),
                                                    Projections.computed(
                                                            "hoursOpen",
                                                            new Document("$subtract", Arrays.asList("$closingHour", "$openHour")))
                                            )
                                    ),
                                    Aggregates.group("$category", Accumulators.avg("value", "$hoursOpen"))
                            )
                    ).spliterator(),
                    false).collect(Collectors.toList());
        }
    }
}
