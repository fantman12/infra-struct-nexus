package io.cloudtrailsqs.event.cloudtrail;

import com.amazonaws.services.cloudtrail.processinglibrary.interfaces.ProgressReporter;
import com.amazonaws.services.cloudtrail.processinglibrary.progress.ProgressStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

/**
 * AWS CloudTrail Reporting data Polling
 */
public class TrailProgressReporter implements ProgressReporter {
    private static final Log logger = LogFactory.getLog(TrailProgressReporter.class);

    public Object reportStart(ProgressStatus var1) {
        return new Date();
    }

    public void reportEnd(ProgressStatus var1, Object var2) {
    }
}
