package com.example.vrusn.bletest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

/**
 * Created by vrusn on 2016-07-07.
 */
public class BLEScanner {

    private static final long SCAN_PERIOD = 10000;

    private MainActivity mainActivity;

    private long scanPeriod;
    private int signalStrength;

    private boolean scanning;
    private Handler handler;

    private BluetoothAdapter bluetoothAdapter;

    public BLEScanner(MainActivity mainActivity, long scanPeriod, int signalStrength) {
        this.mainActivity = mainActivity;
        this.scanPeriod = scanPeriod;
        this.signalStrength = signalStrength;

        handler = new Handler();

        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager = (BluetoothManager) mainActivity.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
    }

    public boolean isScanning() {
        return scanning;
    }

    // If you want to scan for only specific types of peripherals,
    // you can instead call startLeScan(UUID[], BluetoothAdapter.LeScanCallback),
    // providing an array of UUID objects that specify the GATT services your app supports.
    public void scanLeDevice(final boolean enable) {
        if (enable && !scanning) {
            Utils.toast(mainActivity.getApplicationContext(), "Starting BLE scan...");

            // Stops scanning after a pre-defined scan period.
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Utils.toast(mainActivity.getApplicationContext(), "Stopping BLE scan...");

                    scanning = false;
                    bluetoothAdapter.stopLeScan(leScanCallback);

                    scanLeDevice(false);
                }
            }, scanPeriod);

            scanning = true;
            bluetoothAdapter.startLeScan(leScanCallback);
//            mBluetoothAdapter.startLeScan(uuids, mLeScanCallback);
        }
        else {
            scanning = false;
            bluetoothAdapter.stopLeScan(leScanCallback);
        }
    }

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            Utils.toast(mainActivity.getApplicationContext(), "Found new device");
            Log.e("Device found", device.getName() + " " + device.getAddress());

            if(device.getName().equals("My peripheral")) {
                scanLeDevice(false);
                Log.e("Scaning stopped", "true");

                BLEDevice bleDevice = new BLEDevice(device);
                mainActivity.addDevice(bleDevice);
                mainActivity.connectDevice();
            }
        }
    };
}
