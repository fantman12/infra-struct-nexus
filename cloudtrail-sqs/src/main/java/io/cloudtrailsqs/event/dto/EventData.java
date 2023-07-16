package io.cloudtrailsqs.event.dto;

import com.amazonaws.services.cloudtrail.processinglibrary.model.CloudTrailEventData;
import io.cloudtrailsqs.event.cloudtrail.EventNameEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
public class EventData {
    private LocalDateTime eventTime;
    private Object eventData;
    private String eventSource;
    private String eventName;
    private Object userName;
    private int alertType;
    private int dangerLevel;

    private Object alertContent;
    private int cldType;

    private Object iamSn;
    /**
     * By Alert Construct
     * @param cloudTrailEventData
     * @param awsIam
     * @param alertType
     */
    public EventData(CloudTrailEventData cloudTrailEventData, Object awsIam, int alertType,
                     List<AlertRes> alerts) {
        this.eventTime = this.convertDateToLocal(cloudTrailEventData.getEventTime()); // 탐지 일시

        this.userName = awsIam;
        this.eventName = cloudTrailEventData.getEventName();
        this.eventSource = Arrays.stream(EventNameEnum.values())
                .filter(name -> name.value.equals(cloudTrailEventData.getEventName()))
                .map(eName -> eName.source)
                .findFirst()
                .orElse(StringUtils.EMPTY);

        this.eventData = cloudTrailEventData;
        this.alertType = alertType;
        this.dangerLevel = 0;
        this.cldType = 1;
        this.iamSn = awsIam;

        if (!Objects.isNull(alerts)) {
            this.migrateDangerLevel(alerts);
        }
    }

    LocalDateTime convertDateToLocal(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    private void migrateDangerLevel(List<AlertRes> list) {
        for (AlertRes alert : list) {
            if (alert.getMatchTrailEventSource().equals(this.eventSource)
                    && alert.getMatchTrailEventName(this.eventName).equals(this.eventName)) {
                this.dangerLevel = alert.getDangerLevel();
            }
        }
    }
}
