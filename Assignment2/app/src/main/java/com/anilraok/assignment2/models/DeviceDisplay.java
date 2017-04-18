package com.anilraok.assignment2.models;

import com.anilraok.assignment2.screens.MainActivity;

import org.fourthline.cling.model.meta.Device;

/**
 * Created by Anil on 14-04-2017.
 */
public class DeviceDisplay {

    Device device;

    public DeviceDisplay() {
    }

    public DeviceDisplay(Device device) {
        this.device = device;
    }

    public Device getDevice() {
        return device;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceDisplay that = (DeviceDisplay) o;

        if (that.device.getIdentity().getUdn().getIdentifierString().equals(device.getIdentity().getUdn().getIdentifierString()))
            return true;
        return device.equals(that.device);
    }

}
