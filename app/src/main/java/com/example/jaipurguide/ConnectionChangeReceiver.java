package com.example.jaipurguide;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class ConnectionChangeReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent arg1) {

        context.sendBroadcast(new Intent("INTERNET_LOST"));

    }
}