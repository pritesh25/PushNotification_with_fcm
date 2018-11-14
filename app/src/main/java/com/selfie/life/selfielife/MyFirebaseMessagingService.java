package com.selfie.life.selfielife;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    Context context;
    int notificationsId = 0;
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName() ;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0)  {
            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("body");
              String messa = remoteMessage.getData().get("icon");
            Log.e(TAG,"Getmessage"+messa);
            String click_action = remoteMessage.getNotification().getClickAction();//get click_action
            Intent intent = new Intent("com.firebase_FCM-MESSAGE");//firebase's broadcast receiver

           if(click_action.equals("MAINACTIVITY")){
            intent.putExtra("title",title);
            intent.putExtra("message", message);
            intent.putExtra("icon",messa);
           }

           else{
                intent.putExtra("title",title);
                intent.putExtra("message", message);
            }
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
            localBroadcastManager.sendBroadcast(intent);
            // Check if message contains a data payload.
           // sendNotification(title, message, click_action);


        }
    }

    private void sendNotification(String title, String messageBody, String click_action) {
        Intent intent;

        if(click_action.equals("MAINACTIVITY")){
            intent = new Intent(this, MainActivity.class);
            intent.putExtra("Key1",title);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        else if(click_action.equals("LASTWINDOW")){

            intent = new Intent(this, MainActivity.class);
            intent.putExtra("Key1",title);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        }

        else{
            intent = new Intent(this, MainActivity.class);
            intent.putExtra("Key1",title);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

     /*   PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 *//* Request code *//*, intent, PendingIntent.FLAG_UPDATE_CURRENT);

       // Uri defaultSoundUri=  Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.mysoundmp3);
        android.support.v4.app.NotificationCompat.Builder notificationBuilder = new android.support.v4.app.NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(false)
                //.setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0  , notificationBuilder.build());*/


        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.customnotification);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.mipmap.ic_launcher)
                // Set Ticker Message
                .setTicker(title)
                // Dismiss Notification
                .setAutoCancel(true)
                // Set PendingIntent into Notification
                .setContentIntent(pIntent)
                // Set RemoteViews into Notification
                .setContent(remoteViews);

        // Locate and set the Image into customnotificationtext.xml ImageViews
        remoteViews.setImageViewResource(R.id.imagenotileft,R.mipmap.ic_launcher);
        remoteViews.setImageViewResource(R.id.imagenotiright,R.mipmap.ic_launcher);


        // Locate and set the Text into customnotificationtext.xml TextViews
        remoteViews.setTextViewText(R.id.title,title);
        remoteViews.setTextViewText(R.id.text,messageBody);

        // Create Notification Manager
        NotificationManager notificationmanager1 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);



        Notification notification = builder.build();
        notification.contentIntent = pIntent;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;

        // Build Notification with Notification Manager
        notificationmanager1.notify(0, builder.build());
        }





}