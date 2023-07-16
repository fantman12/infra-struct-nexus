package io.cloudtrailsqs.event.cloudtrail;

public enum EventSourceEnum {
    EC2_EVENTS("ec2.amazonaws.com"),
    LIGHTSAIL_EVENTS("lightsail.awsamazon.com"),
    LAMBDA_EVENTS("lambda.amazonaws.com"),
    BATCH_EVENTS("batch.amazonaws.com"),
    ECS_EVENTS("ecs.amazonaws.com"),
    ECR_EVENTS("ecr.amazonaws.com"),
    ECR_PUBLIC_EVENTS("ecr-public.amazonaws.com"),
    RDS_EVENTS("rds.amazonaws.com"),
    ELASTICACHE_EVENTS("elasticache.amazonaws.com"),
    S3_EVENTS("s3.amazonaws.com"),
    ;

    public final String value;
    EventSourceEnum(String value) {
        this.value = value;
    }

    public String eventSource() {
        return this.value;
    }

}
