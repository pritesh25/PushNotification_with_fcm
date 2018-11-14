package com.selfie.life.selfielife;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import static com.selfie.life.selfielife.MyConfiguration.BASE_URL_File;

/**
 * JobService to be scheduled by the JobScheduler.
 * start another service
 */
@SuppressLint("NewApi")
public class TestJobService extends JobService {
    //private static final String TAG = "SyncService";
    private static final String TAG = TestJobService.class.getSimpleName() ;
    String  str_table_name,
            str_id,
            str_thumb_imge,
            str_username;
    String CHANNEL_ID = "my_channel_01";
    int notification_id = 0;
    Bitmap bitmap;

    @Override
    public boolean onStartJob(JobParameters params) {
     //   createNotificationChannel();

       // fetchingFriendProfileData("6");

        Intent service = new Intent(getApplicationContext(), MainActivity.class);
        getApplicationContext().startService(service);
       // Toast.makeText(this, "job started", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"Pritesh Notification");
        Util.scheduleJob(getApplicationContext()); // reschedule the job
        return true;

    }

    @Override
    public boolean onStopJob(JobParameters params) {
       // Util.scheduleJob(getApplicationContext());
        return true;
    }



    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    private void fetchingFriendProfileData(String str_myUser_id) {
//        pb_friend_profile_picture.setVisibility(View.VISIBLE);

        //  RequestBody token = RequestBody.create(MediaType.parse("text/plain"), MyConfiguration.getPreferences(getContext(),PROFILE_TOKEN));
        RequestBody token = RequestBody.create(MediaType.parse("text/plain"),"p+eN45whyGL9HnPqX/mttPgq+Hmv2Fp4rPbUVzo9j+mth7FwGVTWY+hgQ4LsdKEE+xX2uB/JyBaoGB6qFUZQ1A==");
        RequestBody user_id         = RequestBody.create(MediaType.parse("text/plain"), str_myUser_id);
        fetchingSinglePostDataInterface service = MyConfiguration.getRetrofit(MyConfiguration.BASE_URL).create(fetchingSinglePostDataInterface.class);
        Call<ResponseBody> req = service.postImage(token,user_id);
        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                String output="";
                try
                {
                    output = response.body().string();
                    Log.d(TAG,"Friend Profile output > " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    boolean res = Boolean.parseBoolean(jsonObject.getString("result"));
                    // String str_end = jsonObject.getString("endless_info");

                    JSONArray subArray = jsonObject.getJSONArray("endless_info");

                    for (int i = 0; i < subArray.length(); i++) {

                        JSONObject jsonObject1 = subArray.getJSONObject(i);

                        str_table_name        = jsonObject1.getString("table_name");
                        str_id         = jsonObject1.getString("post_id");
                        str_thumb_imge         = jsonObject1.getString("post_thumb_image1");
                        str_username        = jsonObject1.getString("username");

                     //   Toast.makeText(getApplicationContext(), ""+str_id, Toast.LENGTH_SHORT).show();


                        if (str_table_name.equalsIgnoreCase("post")){
                           // showNotification("Notification","hillll");

                         //   displayNotification(str_thumb_imge,str_username);



                        }

                        else if (str_table_name.equalsIgnoreCase("story")){
                          //  showNotification("Notification","hillll");

                        }

                        Log.d(TAG,"Endless output >"+str_id);



                    }

                } catch (JSONException e){
                    e.printStackTrace();     Log.d(TAG,"JSON Exception = "+e.getMessage().toString());
//                    pb_friend_profile_picture.setVisibility(View.GONE);
                }catch (IOException e){
                    e.printStackTrace();    Log.d(TAG,"IO Exception = "+e.getMessage().toString());
                    //   pb_friend_profile_picture.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t){
                Log.d(TAG,"Retrofit Failure");
            }
        });
    }

    public interface fetchingSinglePostDataInterface {
        @Multipart
        @POST(BASE_URL_File+"/check_push_notification.php")
        Call<ResponseBody> postImage(
                @Part("token")              RequestBody token,
                @Part("user_id")            RequestBody user_id );
    }

    private void displayNotification(String imgurl, String usename) {

        //notification_id++;

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Intent snoozeIntent = new Intent(this, MainActivity.class);
        snoozeIntent.setAction("MyBroadCast");
        snoozeIntent.putExtra("snooz", "mark as read");
        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, snoozeIntent, 0);

        Intent intentMessage = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntentMessage = PendingIntent.getActivity(this, 0, intentMessage, 0);

//        String replyLabel = getResources().getString(R.string.reply_label);
//        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
//                .setLabel(replyLabel)
//                .build();

        //PendingIntent replyPendingIntent = PendingIntent.getBroadcast(getApplicationContext(),replyRequestCode,PendingIntent.FLAG_UPDATE_CURRENT);

//        NotificationCompat.Action action =
//                new NotificationCompat.Action.Builder(R.drawable.ic_launcher_foreground,
//                        getString(R.string.label), replyPendingIntent)
//                        .addRemoteInput(remoteInput)
//                        .build();

        //send broadcast receiver to detect removal of notification
        Intent intent1 = new Intent(this, MyStartServiceReceiver.class);
        intent1.setAction("MyBroadCast");
        intent1.putExtra("values","notification is remove");
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getApplicationContext(), 0, intent1, 0);


        //getImageUrl




        //Drawable d = new BitmapDrawable(getResources(), image);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Selfielife")
                .setContentText(usename)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setContentIntent(pendingIntentMessage)
                //.addAction(R.drawable.ic_launcher_foreground, getString(R.string.read),snoozePendingIntent)
                //.addAction(R.drawable.ic_launcher_foreground, getString(R.string.reply),snoozePendingIntent)
                //.addAction(action)
                .setDeleteIntent(pendingIntent1)
                //.setDeleteIntent(snoozePendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notification_id, mBuilder.build());

    }
}