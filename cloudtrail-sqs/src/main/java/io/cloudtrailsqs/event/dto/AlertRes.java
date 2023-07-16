package io.cloudtrailsqs.event.dto;

import io.cloudtrailsqs.event.cloudtrail.EventNameEnum;
import io.cloudtrailsqs.event.util.AlertConfHostBitConstant;
import io.cloudtrailsqs.event.util.CloudTrailConstant;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
public class AlertRes {
    private BigInteger alertConfItemSn;
    private BigInteger parentItemSn;
    private String itemName;
    private String useFlag;
    private int alertTypeCode;
    private int dangerLevel;

    public String getMatchTrailEventSource() {
        if (this.itemName.equals(CloudTrailConstant.CMPL_NAME)) {
            return this.itemName;
        }
        return StringUtils.upperCase(this.itemName);
    }

    public String getMatchTrailEventName(String eventName) {
        List<Integer> integers = Arrays.stream(AlertConfHostBitConstant.ACI_ARR)
                .filter(bit -> this.getCheck(this.alertTypeCode, bit))
                .collect(Collectors.toList());

        for (Integer integer : integers) {
            Optional<EventNameEnum> optionalEventName =
                    EventNameEnum.getEventNameByValue(integer, eventName);
            if (optionalEventName.isPresent()) {
                return optionalEventName.get().value;
            }
        }

        return StringUtils.EMPTY;
    }

    public boolean getCheck(int value, int field) {
        return (value & field) != 0;
    }
}
