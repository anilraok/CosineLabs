package com.anilraok.assignment2.screens.mvp;

import com.anilraok.assignment2.models.DeviceDisplay;
import com.anilraok.assignment2.screens.interfaces.MainActivityModelInterface;
import com.anilraok.assignment2.screens.interfaces.MainActivityViewInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anil on 14-04-2017.
 */

public class MainActivityPresenter {
    private MainActivityViewInterface mainActivityViewInterface;
    private MainActivityModelInterface mainActivityModel;

    public MainActivityPresenter(MainActivityViewInterface mainActivityViewInterface, MainActivityModelInterface mainActivityModel) {
        this.mainActivityViewInterface = mainActivityViewInterface;
        this.mainActivityModel = mainActivityModel;
    }

    public void loadCurrentWifiDetails() {
        if (mainActivityModel.isWifiDetailsPresent()) {
            mainActivityViewInterface.showCurrentWifiName(mainActivityModel.getCurrentWifiName());
            mainActivityViewInterface.showCurrentIpAddress(mainActivityModel.getCurrentWifiIpAddress());
            mainActivityViewInterface.showWifiView();
        }
        else {
            mainActivityViewInterface.hideWifiView();
        }
    }

    public void loadOtherDevicesInNetwork() {
        List<DeviceDisplay> devices = mainActivityModel.getOtherDevicesInCurrentNetwork();

        if (devices != null && devices.size() > 0){
            mainActivityViewInterface.showOtherDevices(devices);
        }
        else
            mainActivityViewInterface.showNoDevicesView();
    }

    public void deviceListUpdated(ArrayList<DeviceDisplay> devices) {
        if (devices != null && devices.size() > 0){
            mainActivityViewInterface.showOtherDevices(devices);
        }
        else
            mainActivityViewInterface.showNoDevicesView();
    }

    public void onResume() {
        mainActivityModel.onResume();
    }

    public void onStop() {
        mainActivityModel.onStop();
    }
}
