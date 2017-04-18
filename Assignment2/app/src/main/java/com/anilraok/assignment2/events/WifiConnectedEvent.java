package com.anilraok.assignment2.events;

import android.net.NetworkInfo;

/**
 * Created by Anil on 14-04-2017.
 */

public class WifiConnectedEvent {
    public NetworkInfo networkInfo;

    public WifiConnectedEvent(NetworkInfo networkInfo) {
        this.networkInfo = networkInfo;
    }
}
