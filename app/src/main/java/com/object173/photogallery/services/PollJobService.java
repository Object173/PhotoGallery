package com.object173.photogallery.services;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public final class PollJobService extends JobService {

    private static final String TAG = "PollJobService";
    private static final int JOB_ID = 173;

    private PollTask mPollTask;

    @Override
    public boolean onStartJob(final JobParameters params) {
        mPollTask = new PollTask();
        mPollTask.execute(params);
        return true;
    }

    @Override
    public boolean onStopJob(final JobParameters params) {
        if(mPollTask != null) {
            mPollTask.cancel(true);
        }
        return true;
    }

    static void setServiceAlarm(final Context context, final boolean isOn) {
        if(isOn == isServiceAlarmOn(context)) {
            return;
        }
        final JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        if(!isOn) {
            scheduler.cancel(JOB_ID);
            return;
        }

        final JobInfo jobInfo = new JobInfo.Builder(
                JOB_ID, new ComponentName(context, PollJobService.class))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPeriodic(PollServiceAdapter.getInterval())
                .setPersisted(true)
                .build();
        scheduler.schedule(jobInfo);
    }

    static boolean isServiceAlarmOn(final Context context) {
        final JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        for (JobInfo jobInfo : scheduler.getAllPendingJobs()) {
            if (jobInfo.getId() == JOB_ID) {
                return true;
            }
        }
        return false;
    }

    private class PollTask extends AsyncTask<JobParameters, Void, Void> {

        @Override
        protected Void doInBackground(final JobParameters... jobParameters) {
            PollServiceAdapter.checkNewItems(PollJobService.this, TAG);
            jobFinished(jobParameters[0], false);
            return null;
        }
    }
}
