package com.selfie.life.selfielife;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;




public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private FirebaseAuth mAuth;
    String regId;
    private static final String TAG = MainActivity.class.getSimpleName() ;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frameLayout, new FragmentNotification(), new FragmentNotification().getTag());
        transaction.addToBackStack(new FragmentNotification().getTag());
        transaction.commit();
       // Toast.makeText(this, "hii", Toast.LENGTH_SHORT).show();
        mAuth = FirebaseAuth.getInstance();
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    //new push notification is received
                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                    // txtMessage.setText(message);
                }
            }
        };
        displayFirebaseRegId();

// Realtime get Notification Data

        LocalBroadcastManager.getInstance(this).registerReceiver(mHandler,new IntentFilter("com.firebase_FCM-MESSAGE"));

        /*if (getIntent().getExtras() != null) {

            for (String key : getIntent().getExtras().keySet())

            {
                if (key.equals("title"))
                   Log.e(TAG,"Notifytitle"+getIntent().getExtras().getString(key));
                    //Title.setText(getIntent().getExtras().getString(key)+userid);
                else if(key.equals("body"))
                    //Message.setText(getIntent().getExtras().getString(key));
                Log.e(TAG,"Notifytitle"+getIntent().getExtras().getString(key));
            }

        }*/


    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        regId = pref.getString("regId", null);

        //   Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId)) {

            Log.e(TAG,"FirebaseId1:" + regId);

            Toast.makeText(this, ""+regId, Toast.LENGTH_SHORT).show();


        } else {

            Toast.makeText(this, "Firebase Reg Id is not received yet!", Toast.LENGTH_SHORT).show();
        }
    }

    private BroadcastReceiver mHandler = new BroadcastReceiver(){
        @Override
        public  void onReceive(Context context, Intent intent){
            String title = intent.getStringExtra("title");
            String message = intent.getStringExtra("message");
          Log.e(TAG,"Notify Title and message >   "+title+"\t"+message);
        }
    };

    @Override
    protected  void  onPause(){
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mHandler);
    }
}
