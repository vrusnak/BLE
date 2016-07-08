package com.example.vrusn.bletest;

import java.util.UUID;

/**
 * Created by vrusn on 2016-07-08.
 */
public class Constants {

    public static final int REQUEST_ENABLE_BT = 1;
    public static final UUID Custom_Service_UUID = UUID.fromString("00001111-0000-1000-8000-00805f9b34fb");
    public static final UUID Custom_Characteristic_UUID = UUID.fromString("00002222-0000-1000-8000-00805f9b34fb");

    public static final long SCAN_PERIOD = 50000;

    public static final String CUSTOM_TEXT = "TEST";
}
