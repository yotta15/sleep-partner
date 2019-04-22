package com.example.gzy.test3.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * created by gzy on 2019/4/21.
 * Describle;
 */
public class ScreenOnOffReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.i("application", "Screen went OFF");
            Toast.makeText(context, "screen OFF", Toast.LENGTH_LONG).show();
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Log.i("application", "Screen went ON");

            Toast.makeText(context, "screen ON", Toast.LENGTH_LONG).show();
        }
    }
}
