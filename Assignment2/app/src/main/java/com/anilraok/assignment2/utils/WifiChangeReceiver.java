package com.anilraok.assignment2.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.anilraok.assignment2.events.WifiConnectedEvent;
import com.anilraok.assignment2.events.WifiDisconnectedEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Anil on 14-04-2017.
 */

public class WifiChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            boolean connected = info.isConnected();

            if (connected){
//            if (intent.getBooleanExtra(WifiManager.WIFI_STATE_ENABLED, false)){
//                Log.e("wifi change","connected");
                EventBus.getDefault().post(new WifiConnectedEvent(info));
            } else {
//                Log.e("wifi change","disconnected");
                // wifi connection was lost
                EventBus.getDefault().post(new WifiDisconnectedEvent());
            }
        }
    }

}
