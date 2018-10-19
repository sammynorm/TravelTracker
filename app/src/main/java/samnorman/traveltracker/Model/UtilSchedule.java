package samnorman.traveltracker.Model;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class UtilSchedule {

        public void scheduleJob(Context context) {

            JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
            ComponentName name = new ComponentName(context, SpringPostRequest.class);

                //Hour and ID subject to change with UI implementation
                final int result = jobScheduler.schedule(getJobInfo(123, 1, name));

                //Just for Logging
                if (result == JobScheduler.RESULT_SUCCESS) {
                    Log.d(TAG, "Scheduled job successfully from UtilSchedule");
                }
            }

        //Use JobInfo conversion for API differences
        private JobInfo getJobInfo(final int id, final long hour, final ComponentName name) {
        final JobInfo jobInfo;
        final long interval = TimeUnit.HOURS.toMillis(hour);
        final boolean isPersistent = true;
        final int networkType = JobInfo.NETWORK_TYPE_ANY;

        //API Version discrepancy
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            jobInfo = new JobInfo.Builder(id, name)
                    .setMinimumLatency(interval)
                    .setRequiredNetworkType(networkType)
                    .setPersisted(isPersistent)
                    .build();
        } else {
            jobInfo = new JobInfo.Builder(id, name)
                    .setPeriodic(interval)
                    .setRequiredNetworkType(networkType)
                    .setPersisted(isPersistent)
                    .build();
        }
        return jobInfo;
    }
}
