package com.anilraok.assignment2.screens.interfaces;

import com.anilraok.assignment2.models.DeviceDisplay;

import java.util.List;

/**
 * Created by Anil on 14-04-2017.
 */

public interface MainActivityViewInterface {

    void showCurrentWifiName(String currentWifiName);

    void showCurrentIpAddress(String currentWifiIpAddress);

    void showOtherDevices(List<DeviceDisplay> devices);

    void showWifiView();

    void hideWifiView();

    void showNoDevicesView();
}
