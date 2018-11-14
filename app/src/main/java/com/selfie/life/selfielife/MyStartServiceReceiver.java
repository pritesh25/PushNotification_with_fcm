package com.selfie.life.selfielife;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MyStartServiceReceiver extends BroadcastReceiver {

    private static final String TAG = MyStartServiceReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

//Background service run
        Util.scheduleJob(context);


//Notification Broadcasst
        Bundle bundle = intent.getExtras();
        if(bundle!=null)
        {

            if(bundle.containsKey("values"))
            {
                Toast.makeText(context,""+bundle.getString("values"),Toast.LENGTH_LONG).show();
                Log.d(TAG,""+bundle.getString("values"));
            }
            else
            {
                Log.d(TAG,"value key is null");
            }

            if(bundle.containsKey("snooz"))
            {
                Toast.makeText(context,""+bundle.getString("snooz"),Toast.LENGTH_LONG).show();
                Log.d(TAG,""+bundle.getString("snooz"));
            }
            else
            {
                Log.d(TAG,"snooz key is null");
            }

        }
        else
        {
            Log.d(TAG,"bundle is null");
        }


    }


}