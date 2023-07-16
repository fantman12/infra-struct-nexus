package io.cloudtrailsqs.event.cloudtrail;

import com.amazonaws.services.cloudtrail.processinglibrary.interfaces.EventFilter;
import com.amazonaws.services.cloudtrail.processinglibrary.model.CloudTrailEvent;
import com.amazonaws.services.cloudtrail.processinglibrary.model.CloudTrailEventData;

import java.util.Arrays;

public class TrailEventFilter implements EventFilter {

    @Override
    public boolean filterEvent(CloudTrailEvent cloudTrailEvent) {
        CloudTrailEventData eventData = cloudTrailEvent.getEventData();

        String eventSource = eventData.getEventSource();
        String eventName = eventData.getEventName();

        return (Arrays
                .stream(EventSourceEnum.values())
                .map(EventSourceEnum::eventSource)
                .anyMatch(eventSource::equals) &&
                Arrays.stream(EventNameEnum.values())
                        .map(EventNameEnum::eventName)
                        .anyMatch(eventName::equals)
        );
    }
}
