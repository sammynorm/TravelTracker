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


    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job Started");
        this.params = params;
        UtilSchedule util = new UtilSchedule();

        //Create instance of locationservice(update location)
        gps = new LocationFinder(SpringPostRequest.this);

        //Create Object for Posting
        JSONPojo jsonpojo = new JSONPojo(gps.getLatitude(), gps.getLongitude());

        //POST request to webservice
        Call<JSONPojo> call = RetrofitClient.getInstance().getApi().sendcoordinates(jsonpojo);

        //Used for Debugging(If needed)
        call.enqueue(new Callback<JSONPojo>() {
            @Override
            public void onResponse(Call<JSONPojo> call, Response<JSONPojo> response) { }

            @Override
            public void onFailure(Call<JSONPojo> call, Throwable t) {}
        });

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//if API version doesn't support periodic
            Log.d(TAG, "Scheduling from within API limitaions");
            util.scheduleJob(this);
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
}
