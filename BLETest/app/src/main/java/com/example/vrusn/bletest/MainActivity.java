package com.example.vrusn.bletest;

import android.app.ActionBar;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGatt;

    private BLEScanner bleScanner;
    //private DeviceManager deviceManager;

    private Button btnScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hideStatusBar();

        File file = getFilesDir();
        Log.e("getFileDir", getFilesDir() + "");

        //bleScanner = new BLEScanner(this);

//        ArrayList<BLEDevice> devicesArrayList = new ArrayList<>();
//        deviceManager = new DeviceManager();
//        deviceManager.setDevicesArrayList(devicesArrayList);

        this.btnScan = (Button) findViewById(R.id.btnScan);
        setListeners();

        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE not supported", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, Constants.REQUEST_ENABLE_BT);
        }
    }

    private void hideStatusBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void setListeners() {
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bleScanner.scanLeDevice(true);
            }
        });
    }

//    public void addDevice(BLEDevice device) {
//        deviceManager.getDevicesArrayList().add(device);
//    }

    public void connectDevice(BLEDevice device) {
        bluetoothGatt = device.getDevice().connectGatt(getApplicationContext(), false, btleGattCallback);
        bluetoothGatt.connect();
    }

    private final BluetoothGattCallback btleGattCallback = new BluetoothGattCallback() {

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
        }

        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.e("Connected", gatt.getDevice().getName());
                Log.e("Address", gatt.getDevice().getAddress());

                Log.e("Discovering services", "...");
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.e("Device disconnected", gatt.getDevice().getName());
            }
        }

        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
            if (gatt == null)
                Log.e("gatt", null);

            List<BluetoothGattService> services = bluetoothGatt.getServices();

//            for (BluetoothGattService service : services) {
//                Log.e("Service UUID", service.getUuid().toString());
//                List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
//                for (BluetoothGattCharacteristic characteristic : characteristics)
//                    Log.e("Charasteristic UUID", characteristic.getUuid().toString());
//                Log.e(" ", " ");
//            }
//
//                        for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
//                //find descriptor UUID that matches Client Characteristic Configuration (0x2902)
//                // and then call setValue on that descriptor
//
//                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//                bluetoothGatt.writeDescriptor(descriptor);
//            }

            Log.e("Service list size", services.size() + "");

            if (services.size() != 0) {
                BluetoothGattService myService = gatt.getService(Constants.Custom_Service_UUID);
                if (myService == null)
                    Log.e("Ex", "Custom service not found!");
                else
                    Log.e("Custom service found!", myService.getUuid().toString());

                if (myService != null) {
                    BluetoothGattCharacteristic myCharacteristic = myService.getCharacteristic(Constants.Custom_Characteristic_UUID);
                    if (myCharacteristic == null)
                        Log.e("Ex", "Custom characteristic not found!");
                    else
                        Log.e("Custom charac. found!", myCharacteristic.getUuid().toString());

                    writeToCharacteristic(myCharacteristic, gatt, Constants.CUSTOM_TEXT);
                    readFromCharacteristic(myService);
                }
            }
        }
    };

    private void writeToCharacteristic(BluetoothGattCharacteristic characteristic, BluetoothGatt gatt, String str) {
        byte[] value = str.getBytes();
        characteristic.setValue(value);
        Log.e("Successful write", gatt.writeCharacteristic(characteristic) + "");
        Log.e("Sent", str);
    }

    private void readFromCharacteristic(BluetoothGattService service) {
        BluetoothGattCharacteristic readCharacteristic = service.getCharacteristic(Constants.Custom_Characteristic_UUID);
        byte[] data = readCharacteristic.getValue();
        String recieved = new String(data, StandardCharsets.UTF_8);
        Log.e("Recieved", recieved);
    }

    private void closeConnection() {
        bluetoothGatt.disconnect();
        bluetoothGatt.close();
    }
}
