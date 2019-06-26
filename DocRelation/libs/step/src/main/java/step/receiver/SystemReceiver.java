package step.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import step.StepManager;
import step.StepService;


/**
 * Created by yuwentao on 16/6/24.
 */
public class SystemReceiver extends BroadcastReceiver {

    private static final String TAG = "StepService";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.d(TAG, "SystemReceiver   " + intent.getAction());
        Intent i = new Intent();
        i.setClass(StepManager.getmContext(), StepService.class);
        StepManager.getmContext().startService(i);
        //PollingUtils.getIns().startPollingService(context, 5, StepService.class, StepService.ACTION);

    }
}
