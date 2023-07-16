package io.cloudtrailsqs.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.lang.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import java.util.Objects;

@Configuration
public class MongoDocumentConfigure extends AbstractMongoClientConfiguration
{
    @Value("${spring.data.mongodb.username}")
    private String userName;

    @Value("${spring.data.mongodb.password}")
    private String password;

    @Value("${spring.data.mongodb.database}")
    private String database;


    public MongoClient initalizeMongoClient;

    @Value("${spring.data.mongodb.uri}")
    private String endpoint;

    @Override
    @NonNull
    public MongoClient mongoClient( ) {
        if(Objects.isNull(initalizeMongoClient)) {
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(endpoint))
                    .credential(credential)
                    .applyToConnectionPoolSettings(builder -> builder.minSize(50).maxSize(100))
                    .build();

            initalizeMongoClient = MongoClients.create(settings);
        }

        return initalizeMongoClient;
    }

    @Override
    @NonNull
    protected String getDatabaseName()
    {
        return this.database;
    }


    public MongoClient getMongoClient() {
        return this.initalizeMongoClient;
    }


    private final MongoCredential credential = MongoCredential.createCredential(userName, getDatabaseName(),
            password.toCharArray());

    @Bean
    public MongoDatabaseFactory mongoDbFactory() {
        return new SimpleMongoClientDatabaseFactory(mongoClient(),  getDatabaseName());
    }
}
