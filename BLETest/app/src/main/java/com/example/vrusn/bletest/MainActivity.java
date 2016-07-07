package com.example.vrusn.bletest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static int REQUEST_ENABLE_BT = 1;

    private BluetoothAdapter bluetoothAdapter;
    private BLEScanner bleScanner;
    private BLEDevice bleDevice;
    private DeviceManager deviceManager;

    private BluetoothGatt bluetoothGatt;

    private Button btnScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bleScanner = new BLEScanner(this, 5000, -80);

        this.btnScan = (Button) findViewById(R.id.btnScan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bleScanner.scanLeDevice(true);
            }
        });

        deviceManager = new DeviceManager();
        ArrayList<BLEDevice> devicesArrayList = new ArrayList<>();

        deviceManager.setDevicesArrayList(devicesArrayList);

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
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    public void addDevice(BLEDevice device) {
        deviceManager.getDevicesArrayList().add(device);
    }

    public void connectDevice() {
        BLEDevice myDevice = deviceManager.getDevicesArrayList().get(0);
        bluetoothGatt = myDevice.getBTDevice().connectGatt(getApplicationContext(), false, btleGattCallback);
    }

    private final BluetoothGattCallback btleGattCallback = new BluetoothGattCallback() {

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            // this will get called anytime you perform a read or write characteristic operation

            //read the characteristic data
            byte[] data = characteristic.getValue();
        }

        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
            // this will get called when a device connects or disconnects
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.e("Connected", gatt.getDevice().getName());

                Log.e("Discovering services", "...");
                bluetoothGatt.discoverServices();
            }
        }

        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
            List<BluetoothGattService> services = bluetoothGatt.getServices();

            for (BluetoothGattService service : services) {
                Log.e("Service UUID", service.getUuid().toString());
                List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                for (BluetoothGattCharacteristic characteristic : characteristics)
                    Log.e("Charasteristic UUID", characteristic.getUuid().toString());
                Log.e(" "," ");
            }

//            for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
//                //find descriptor UUID that matches Client Characteristic Configuration (0x2902)
//                // and then call setValue on that descriptor
//
//                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//                bluetoothGatt.writeDescriptor(descriptor);
//            }
        }
    };

    private void closeConnection() {
        bluetoothGatt.disconnect();
        bluetoothGatt.close();
    }
}
