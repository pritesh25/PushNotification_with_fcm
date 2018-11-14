package com.selfie.life.selfielife;

        import android.annotation.SuppressLint;

        import android.app.job.JobInfo;
        import android.app.job.JobScheduler;

        import android.content.ComponentName;
        import android.content.Context;

        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;

        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;



public class FragmentNotification extends Fragment
{



    private static final String TAG = FragmentNotification.class.getSimpleName() ;
    @SuppressLint("NewApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification,container,false);

        @SuppressLint({"NewApi", "LocalSuppress"}) JobScheduler jobScheduler = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            jobScheduler = (JobScheduler) getActivity().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        }

        @SuppressLint({"NewApi", "LocalSuppress"}) JobInfo jobInfo = new JobInfo.Builder(11, new ComponentName(getActivity(), TestJobService.class))
                // only add if network access is required
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build();

        jobScheduler.schedule(jobInfo);




        init(view);
        return view;
    }

    private void init(View view) {



    }





}
