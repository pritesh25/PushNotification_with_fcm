package com.selfie.life.selfielife;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity1 extends Activity {
    private static final String TAG = MainActivity1.class.getSimpleName() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitymain1);


         if (getIntent().getExtras() != null) {

            for (String key : getIntent().getExtras().keySet())

            {
                if (key.equals("icon"))
                   Log.e(TAG,"Notifyicon"+getIntent().getExtras().getString(key));
                    //Title.setText(getIntent().getExtras().getString(key)+userid);
                else if(key.equals("body"))
                    //Message.setText(getIntent().getExtras().getString(key));
                Log.e(TAG,"Notifytitle"+getIntent().getExtras().getString(key));
            }

        }

    }
}