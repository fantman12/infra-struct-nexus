package io.cloudtrailsqs.event;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudtrail.processinglibrary.AWSCloudTrailProcessingExecutor;
import com.amazonaws.services.cloudtrail.processinglibrary.configuration.ClientConfiguration;
import io.cloudtrailsqs.config.MongoConfig;
import io.cloudtrailsqs.event.cloudtrail.TrailEventFilter;
import io.cloudtrailsqs.event.cloudtrail.TrailEventsProcessor;
import io.cloudtrailsqs.event.cloudtrail.TrailProgressReporter;
import io.cloudtrailsqs.event.dto.AlertRes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReadyTrailEvent {

    private final MongoConfig mongoConfig;

    public void trailReadyEvent(String domain, String target,
                                Object awsIam, List<AlertRes> alerts) {
        ClientConfiguration basicConfig = new ClientConfiguration(
                domain.concat(String.valueOf(awsIam)).concat(target),
                new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(
                                String.valueOf(awsIam),
                                String.valueOf(awsIam)
                        )
                )
        );

        basicConfig.setSqsRegion(Region.AP_NORTHEAST_2.id());
        basicConfig.setS3Region(Region.AP_NORTHEAST_2.id());
        basicConfig.setVisibilityTimeout(60);
        basicConfig.setThreadCount(10);
        basicConfig.setNumOfParallelReaders(15);
        basicConfig.setEnableRawEventInfo(true);
        basicConfig.setMaxEventsPerEmit(10000);

        new AWSCloudTrailProcessingExecutor
                .Builder(new TrailEventsProcessor(mongoConfig, awsIam, alerts), basicConfig)
                .withProgressReporter(new TrailProgressReporter())
                .withEventFilter(new TrailEventFilter())
                .build()
                .start();
    }
}
