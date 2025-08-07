package dst.ass1.doc.impl;

import com.mongodb.client.*;
import com.mongodb.client.model.Indexes;
import dst.ass1.doc.IDocumentRepository;
import dst.ass1.jpa.model.ILocation;
import dst.ass1.jpa.util.Constants;
import org.bson.Document;

import java.util.Map;

public class DocumentRepository implements IDocumentRepository {
    @Override
    public void insert(ILocation location, Map<String, Object> locationProperties) {
        try (MongoClient mongoClient = MongoClients.create()) {
            MongoDatabase database = mongoClient.getDatabase(Constants.MONGO_DB_NAME);
            MongoCollection<Document> collection = database.getCollection(Constants.COLL_LOCATION_DATA);

            // Create indexes here for lack of a better place
            collection.createIndex(Indexes.geo2dsphere("geo"));
            collection.createIndex(Indexes.ascending("location_id"));

            Document newDoc = new Document()
                    .append(Constants.I_LOCATION, location.getLocationId())
                    .append(Constants.M_LOCATION_NAME, location.getName());
            newDoc.putAll(locationProperties);
            collection.insertOne(newDoc);
        }
    }
}
