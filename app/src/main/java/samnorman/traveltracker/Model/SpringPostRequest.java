package samnorman.traveltracker.Model;


import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpringPostRequest extends JobService {
    private static final String TAG = SpringPostRequest.class.getSimpleName();
    LocationFinder gps;
    JobParameters params;
    UtilSchedule util;


    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job Started");
        this.params = params;

        //Create instance of locationservice(update location)
        gps = new LocationFinder(SpringPostRequest.this);//

        //Create Object for Posting
        JSONPojo jsonpojo = new JSONPojo(gps.getLatitude(), gps.getLongitude());

        //POST request to webservice
        Call<JSONPojo> call = RetrofitClient.getInstance().getApi().sendcoordinates(jsonpojo);

        //Used for Debugging(If needed)
        call.enqueue(new Callback<JSONPojo>() {
            @Override
            public void onResponse(Call<JSONPojo> call, Response<JSONPojo> response) {}

            @Override
            public void onFailure(Call<JSONPojo> call, Throwable t) {}
        });

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//if API version doesn't support periodic
            Log.d(TAG, "Scheduling from within API limitaions");
            util.scheduleJob(getApplicationContext());
       //     scheduleJob();//reschedule job to run
        } else {
            Log.d(TAG, "No API Limiations, running JobFinished");
            jobFinished(params, true);
            // return true if using background thread else return false
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        // reschedule job return true else return false
        return false;
    }

/*    private void scheduleJob() {
        final JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        final ComponentName name = new ComponentName(this, SpringPostRequest.class);

        //Hour and ID subject to change with UI
        final int result = jobScheduler.schedule(getJobInfo(123, 1, name));

        if (result == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Scheduled job successfully from SpringPostRequest");
        }
    }*/


    //Set Job Details and ensure call is correct for API
/*    private JobInfo getJobInfo(final int id, final long hour, final ComponentName name) {
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
    }*/



    public void scheduleIfNot( Context context ) {
        JobScheduler scheduler = (JobScheduler) context.getSystemService( Context.JOB_SCHEDULER_SERVICE ) ;

        for ( JobInfo jobInfo : scheduler.getAllPendingJobs() ) {
            if ( jobInfo.getId() == 123 ) {
                Log.d(TAG, "Job found");
                System.out.println(jobInfo.getId());
                util.scheduleJob(context);
            }
        }
    }
}
