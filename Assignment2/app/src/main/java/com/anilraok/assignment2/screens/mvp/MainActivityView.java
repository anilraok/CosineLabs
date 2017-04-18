package com.anilraok.assignment2.screens.mvp;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anilraok.assignment2.R;
import com.anilraok.assignment2.adapters.DevicesAdapter;
import com.anilraok.assignment2.models.DeviceDisplay;
import com.anilraok.assignment2.screens.MainActivity;
import com.anilraok.assignment2.screens.interfaces.MainActivityViewInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Anil on 14-04-2017.
 */

public class MainActivityView implements MainActivityViewInterface {
    private MainActivity mainActivity;

    @BindView(R.id.wifi_name)
    public TextView wifiName;

    @BindView(R.id.wifi_ip_address)
    public TextView wifiIpAddress;

    @BindView(R.id.devices_list)
    public RecyclerView devicesRecyclerView;

    @BindView(R.id.wifi_container)
    public RelativeLayout wifiContainer;

    @BindView(R.id.no_wifi_container)
    public RelativeLayout noWifiContainer;

    @BindView(R.id.no_wifi_view)
    public TextView noWifiView;

    @BindView(R.id.no_devices_view)
    public TextView noDevicesView;

    private ArrayList<DeviceDisplay> deviceDisplays;
    private DevicesAdapter devicesAdapter;
    private LinearLayoutManager linearLayoutManager;

    public MainActivityView(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

        initBindings();
    }

    private void initBindings() {
        ButterKnife.bind(this, mainActivity);

        deviceDisplays = new ArrayList<>();
        devicesAdapter = new DevicesAdapter(mainActivity,deviceDisplays);

        linearLayoutManager = new LinearLayoutManager(mainActivity);

        devicesRecyclerView.setLayoutManager(linearLayoutManager);
        devicesRecyclerView.setAdapter(devicesAdapter);
    }

    @Override
    public void showCurrentWifiName(String currentWifiName) {
        wifiName.setText(currentWifiName);
    }

    @Override
    public void showCurrentIpAddress(String currentWifiIpAddress) {
        wifiIpAddress.setText(currentWifiIpAddress);
    }

    @Override
    public void showOtherDevices(List<DeviceDisplay> devices) {
        deviceDisplays.clear();
        deviceDisplays.addAll(devices);

        devicesAdapter.notifyDataSetChanged();

        noDevicesView.setVisibility(View.GONE);
        devicesRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showWifiView() {
        wifiContainer.setVisibility(View.VISIBLE);
        noWifiContainer.setVisibility(View.GONE);
    }

    @Override
    public void hideWifiView() {
        wifiContainer.setVisibility(View.GONE);
        noWifiContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoDevicesView() {
        noDevicesView.setVisibility(View.VISIBLE);
        devicesRecyclerView.setVisibility(View.GONE);
    }
}
