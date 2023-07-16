package io.cloudtrailsqs.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.lang.NonNull;
import org.bson.types.Decimal128;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.SessionSynchronization;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.lang.NonNullApi;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration
{
    @Value("${spring.data.mongodb.username}")
    private String userName;

    @Value("${spring.data.mongodb.password}")
    private String password;

    @Value("${spring.data.mongodb.database}")
    private String database;

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private int port;

    @Value("${spring.data.mongodb.authentication-database}")
    private String auth;

    MongoClient _mongoClient;

    @Override
    @NonNull
    public MongoClient mongoClient( )
    {

        final MongoCredential credential = MongoCredential.createCredential( userName, getDatabaseName( ),
                password.toCharArray( ) );
        if(Objects.isNull(_mongoClient)) {
            final String url = "mongodb://" + host + ":" + port + "/" + getDatabaseName( );
            ConnectionString connString = new ConnectionString(url);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connString)
                    .credential(credential)
                    .applyToConnectionPoolSettings(builder -> builder.minSize(10).maxSize(100))
                    .applyToSocketSettings(builder -> builder.readTimeout(20000, TimeUnit.MILLISECONDS)
                            .connectTimeout(20000, TimeUnit.MILLISECONDS))
                    .applyToConnectionPoolSettings(builder -> builder.maxWaitTime(1500, TimeUnit.MILLISECONDS))
                    .build();

            _mongoClient = MongoClients.create(settings);
        }

        return _mongoClient;
    }

    @Override
    protected String getDatabaseName( )
    {
        return this.database;
    }


    public MongoClient getMongoClient() {
        return this._mongoClient;
    }


    @Bean
    public MongoDatabaseFactory mongoDbFactory(){
        return new SimpleMongoClientDatabaseFactory(mongoClient( ),  getDatabaseName( ));
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory());
        MappingMongoConverter mappingMongoConverter = new MappingMongoConverter(dbRefResolver, new MongoMappingContext());
        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        mappingMongoConverter.setCustomConversions(customConversions2());
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory(),mappingMongoConverter);
        mongoTemplate.setSessionSynchronization(SessionSynchronization.ON_ACTUAL_TRANSACTION);
        return mongoTemplate;
    }



    public MongoCustomConversions customConversions2() {
        return new MongoCustomConversions(Arrays.asList(
                new BigIntegerIntegerConverter(), new IntegerConverter()
        ));
    }

    @WritingConverter
    public static class BigIntegerIntegerConverter implements Converter<BigInteger, Integer> {

        @Override
        public Integer convert(@NonNull BigInteger source) {
            return source.intValue();
        }
    }

    @ReadingConverter
    public static class IntegerConverter implements Converter<Integer, Integer> {

        @Override
        public Integer convert(@NonNull Integer source) {
            return source;
        }
    }

    @WritingConverter
    public static class BigDecimalDecimal128Converter implements Converter<BigDecimal, Decimal128> {

        @Override
        public Decimal128 convert(@NonNull BigDecimal source) {
            return new Decimal128(source);
        }
    }

    @ReadingConverter
    public static class Decimal128BigDecimalConverter implements Converter<Decimal128, BigDecimal> {

        @Override
        public BigDecimal convert(@NonNull Decimal128 source) {
            return source.bigDecimalValue();
        }
    }
}
