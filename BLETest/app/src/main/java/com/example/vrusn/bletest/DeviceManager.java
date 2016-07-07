package com.example.vrusn.bletest;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;

/**
 * Created by vrusn on 2016-07-07.
 */
public class DeviceManager {

    private ArrayList<BLEDevice> devicesArrayList;

    public ArrayList<BLEDevice> getDevicesArrayList() {
        return devicesArrayList;
    }

    public void setDevicesArrayList(ArrayList<BLEDevice> devicesArrayList) {
        this.devicesArrayList = devicesArrayList;
    }
}
