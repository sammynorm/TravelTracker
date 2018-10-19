package samnorman.traveltracker.Model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static android.content.ContentValues.TAG;


//On boot start service
public class StartupBootReceiver extends BroadcastReceiver {
    LocationFinder gps;

    @Override
    public void onReceive(Context context, Intent intent) {
        UtilSchedule utilBoot = new UtilSchedule();

        //On Boot, start LocationService
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            //Instantiate LocationFinder Service
            gps = new LocationFinder(context);
            utilBoot.scheduleJob(context);

            Log.d(TAG, "BroadcastReceiver started on Boot");
        }
    }
}
