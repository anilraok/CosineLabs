package com.anilraok.assignment2.screens.mvp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import com.anilraok.assignment2.R;
import com.anilraok.assignment2.events.WifiConnectedEvent;
import com.anilraok.assignment2.events.WifiDisconnectedEvent;
import com.anilraok.assignment2.models.DeviceDisplay;
import com.anilraok.assignment2.screens.MainActivity;
import com.anilraok.assignment2.screens.interfaces.MainActivityModelInterface;
import com.anilraok.assignment2.utils.WifiChangeReceiver;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.android.AndroidUpnpServiceImpl;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anil on 14-04-2017.
 */

public class MainActivityModel implements MainActivityModelInterface {

    private WifiChangeReceiver wifiChangeReceiver;
    private ArrayList<DeviceDisplay> deviceDisplays;
    private MainActivity mainActivity;
    private MainActivityPresenter presenter;

    private String ssid;
    private String ipAddress;
    private boolean wifiDetailsPresent;

    private Registry registry;

    private BrowseRegistryListener registryListener = new BrowseRegistryListener();

    private AndroidUpnpService upnpService;

    private ServiceConnection serviceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            upnpService = (AndroidUpnpService) service;
            registry = upnpService.getRegistry();

//            deviceDisplays.clear();
//            devicesAdapter.notifyDataSetChanged();

            for (Device device : registry.getDevices()) {
                registryListener.deviceAdded(device);
            }


            // Getting ready for future device advertisements
            upnpService.getRegistry().addListener(registryListener);

            // Search asynchronously for all devices
            upnpService.getControlPoint().search();
        }

        public void onServiceDisconnected(ComponentName className) {
            upnpService = null;
        }
    };

    protected void searchNetwork() {
        if (upnpService == null) return;
        Toast.makeText(mainActivity, "Searching", Toast.LENGTH_SHORT).show();
        upnpService.getRegistry().removeAllRemoteDevices();
        upnpService.getControlPoint().search();
    }

    public void setPresenter(MainActivityPresenter presenter) {
        this.presenter = presenter;
    }

    public class BrowseRegistryListener extends DefaultRegistryListener {

        /* Discovery performance optimization for very slow Android devices! */

        @Override
        public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {
            deviceAdded(device);
        }

        @Override
        public void remoteDeviceDiscoveryFailed(Registry registry, final RemoteDevice device, final Exception ex) {
            mainActivity.showToast(
                    "Discovery failed of '" + device.getDisplayString() + "': " +
                            (ex != null ? ex.toString() : "Couldn't retrieve device/service descriptors"),
                    true
            );
            deviceRemoved(device);
        }
        /* End of optimization, you can remove the whole block if your Android handset is fast (>= 600 Mhz) */

        @Override
        public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
            deviceAdded(device);
        }

        @Override
        public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
            deviceRemoved(device);
        }

        @Override
        public void localDeviceAdded(Registry registry, LocalDevice device) {
            deviceAdded(device);
        }

        @Override
        public void localDeviceRemoved(Registry registry, LocalDevice device) {
            deviceRemoved(device);
        }

        public void deviceAdded(final Device device) {
            mainActivity.runOnUiThread(new Runnable() {
                public void run() {


                    DeviceDisplay d = new DeviceDisplay(device);

                    if (!deviceDisplays.contains(d)) {
                        deviceDisplays.add(d);
//                        devicesAdapter.notifyDataSetChanged();
                        presenter.deviceListUpdated(deviceDisplays);
                    }

                }
            });
        }

        public void deviceRemoved(final Device device) {
            mainActivity.runOnUiThread(new Runnable() {
                public void run() {
                    if (deviceDisplays != null)
                        deviceDisplays.remove(new DeviceDisplay(device));
//                    listAdapter.remove(new DeviceDisplay(device));
                    presenter.deviceListUpdated(deviceDisplays);
                }
            });
        }
    }

    public MainActivityModel(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

        EventBus.getDefault().register(this);

        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);

        wifiChangeReceiver = new WifiChangeReceiver();
        mainActivity.registerReceiver(wifiChangeReceiver, intentFilter);

        loadCurrentWifiDetails();

        deviceDisplays = new ArrayList<>();
        mainActivity.getApplicationContext().bindService(
                new Intent(mainActivity, AndroidUpnpServiceImpl.class),
                serviceConnection,
                Context.BIND_AUTO_CREATE
        );
    }

    @Override
    public void loadCurrentWifiDetails() {

        ConnectivityManager connManager = (ConnectivityManager) mainActivity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);


        if (networkInfo.isConnected()) {

            Log.e("wifiConnected","yes");
            WifiManager wifiManager = (WifiManager) mainActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && connectionInfo.getSSID() != null && !connectionInfo.getSSID().trim().isEmpty()) {
                ssid = connectionInfo.getSSID();
            }
//            WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ip = wifiInfo.getIpAddress();
            ipAddress = Formatter.formatIpAddress(ip);
            Log.e("currentWifi",ipAddress+" "+ssid);

            wifiDetailsPresent = true;
        }
        else {
            wifiDetailsPresent = false;
        }
    }

    @Override
    public String getCurrentWifiName() {
        return ssid;
    }

    @Override
    public String getCurrentWifiIpAddress() {
        return ipAddress;
    }

    @Override
    public List<DeviceDisplay> getOtherDevicesInCurrentNetwork() {
        return deviceDisplays;
    }

    @Override
    public boolean isWifiDetailsPresent() {
        return wifiDetailsPresent;
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);

        if (wifiChangeReceiver != null)
            mainActivity.unregisterReceiver(wifiChangeReceiver);
    }

    @Override
    public void onResume() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);

        if (wifiChangeReceiver == null)
            wifiChangeReceiver = new WifiChangeReceiver();
        mainActivity.registerReceiver(wifiChangeReceiver, intentFilter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(WifiConnectedEvent connectedEvent){
        loadCurrentWifiDetailsWithData(connectedEvent.networkInfo);
        presenter.loadCurrentWifiDetails();
        presenter.loadOtherDevicesInNetwork();
    }

    private void loadCurrentWifiDetailsWithData(NetworkInfo networkInfo) {
        if (networkInfo.isConnected()) {
            WifiManager wifiManager = (WifiManager) mainActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && connectionInfo.getSSID() != null && !connectionInfo.getSSID().trim().isEmpty()) {
                ssid = connectionInfo.getSSID();
            }
//            WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ip = wifiInfo.getIpAddress();
            ipAddress = Formatter.formatIpAddress(ip);
            Log.e("currentWifi",ipAddress+" "+ssid);

            wifiDetailsPresent = true;
        }
        else {
            wifiDetailsPresent = false;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(WifiDisconnectedEvent disconnectedEvent){
        loadCurrentWifiDetails();
        presenter.loadCurrentWifiDetails();
    }
}
