package com.anilraok.assignment2.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anilraok.assignment2.models.DeviceDisplay;
import com.anilraok.assignment2.R;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.RemoteDeviceIdentity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Anil on 13-04-2017.
 */

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.DeviceViewHolder> {

    private Activity activity;
    private List<DeviceDisplay> deviceDisplayList;

    private static final String DEVICE_NAME_PREFIX = "Device name: ";
    private static final String DEVICE_UDN_PREFIX = "UDN: ";
    private static final String DEVICE_IP_PREFIX = "IP: ";

    public DevicesAdapter(Activity activity, List<DeviceDisplay> deviceDisplayList) {
        this.activity = activity;
        this.deviceDisplayList = deviceDisplayList;
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.devices_item,parent,false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder holder, int position) {
        Device device = deviceDisplayList.get(position).getDevice();

        String name =
                device.getDetails() != null && device.getDetails().getFriendlyName() != null
                        ? device.getDetails().getFriendlyName()
                        : device.getDisplayString();

        holder.deviceName.setText(DEVICE_NAME_PREFIX + name);

        String deviceUDN = device.getIdentity().getUdn().getIdentifierString();

        holder.deviceUDN.setText(DEVICE_UDN_PREFIX + deviceUDN);

        if (device.getDetails().getBaseURL() != null){
            String deviceIP = device.getDetails().getBaseURL().getHost();

            if (deviceIP != null)
                holder.deviceIp.setText(DEVICE_IP_PREFIX + deviceIP);
        }
        else{
            RemoteDeviceIdentity remoteDeviceIdentity = null;
            if (device.getIdentity() instanceof RemoteDeviceIdentity)
                remoteDeviceIdentity = (RemoteDeviceIdentity) device.getIdentity();

            if (remoteDeviceIdentity != null){
                holder.deviceIp.setText(DEVICE_IP_PREFIX + remoteDeviceIdentity.getDiscoveredOnLocalAddress().getHostAddress());
            }
        }


    }

    @Override
    public int getItemCount() {
        return deviceDisplayList.size();
    }

    class DeviceViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.device_ip) public TextView deviceIp;
        @BindView(R.id.device_udn) public TextView deviceUDN;
        @BindView(R.id.device_name) public TextView deviceName;

        public DeviceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
