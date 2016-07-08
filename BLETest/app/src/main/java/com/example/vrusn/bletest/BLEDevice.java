package com.example.vrusn.bletest;

import android.bluetooth.BluetoothDevice;

/**
 * Created by vrusn on 2016-07-07.
 */
public class BLEDevice {

    private BluetoothDevice bluetoothDevice;
    private int rssi;
    private String name, address;

    public BLEDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public String getAddress() {
        return bluetoothDevice.getAddress();
    }

    public String getName() {
        return bluetoothDevice.getName();
    }

    public BluetoothDevice getDevice() {
        return this.bluetoothDevice;
    }

//    public void setRSSI(int rssi) {
//        this.rssi = rssi;
//    }

    public int getRSSI() {
        return rssi;
    }
}
