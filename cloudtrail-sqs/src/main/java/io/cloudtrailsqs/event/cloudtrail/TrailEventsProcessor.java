package io.cloudtrailsqs.event.cloudtrail;

import com.amazonaws.services.cloudtrail.processinglibrary.interfaces.EventsProcessor;
import com.amazonaws.services.cloudtrail.processinglibrary.model.CloudTrailEvent;
import io.cloudtrailsqs.config.MongoDocumentConfigure;
import io.cloudtrailsqs.event.dto.AlertRes;
import io.cloudtrailsqs.event.dto.EventData;
import io.cloudtrailsqs.event.util.CloudTrailConstant;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.InsertOneModel;
import org.bson.Document;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

public class TrailEventsProcessor implements EventsProcessor {
    private final MongoDocumentConfigure MongoDocumentConfigure;
    private final Object awsIam;
    private final List<AlertRes> alerts;

    public TrailEventsProcessor(MongoDocumentConfigure MongoDocumentConfigure, Object awsIam, List<AlertRes> alerts) {
        this.MongoDocumentConfigure = MongoDocumentConfigure;
        this.awsIam = awsIam;

        this.alerts = alerts;
    }

    @Override
    public void process(List<CloudTrailEvent> events) {
        MongoDatabase mongoDatabase = MongoDocumentConfigure.getMongoClient().getDatabase(CloudTrailConstant.ALERT_MONGO_DB);
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(CloudTrailConstant.ALERT_MONGO_COLLECTION);

        for (CloudTrailEvent event : events) {
            Document documents = generateMongoDocs(
                    new JSONObject(new EventData(event.getEventData(), awsIam,
                    CloudTrailConstant.SERVER_ALERT_TYPE, alerts))
            );

            mongoCollection.bulkWrite(
                    Collections.singletonList(new InsertOneModel<>(new Document(documents)))
            );
        }
    }

    public Document generateMongoDocs(Object lines) {
        return Document.parse(String.valueOf(lines));
    }
}
