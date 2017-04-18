package com.anilraok.assignment2.screens.interfaces;

import com.anilraok.assignment2.models.DeviceDisplay;

import java.util.List;

/**
 * Created by Anil on 14-04-2017.
 */

public interface MainActivityModelInterface {

    void loadCurrentWifiDetails();

    String getCurrentWifiName();

    String getCurrentWifiIpAddress();

    List<DeviceDisplay> getOtherDevicesInCurrentNetwork();

    boolean isWifiDetailsPresent();

    void onStop();

    void onResume();
}
