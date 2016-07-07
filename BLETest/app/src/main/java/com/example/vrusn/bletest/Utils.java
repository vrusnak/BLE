package com.example.vrusn.bletest;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by vrusn on 2016-07-07.
 */
public class Utils {

    public static void toast(Context context, String string) {
        Toast toast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER | Gravity.BOTTOM, 0, 0);
        toast.show();
    }
}
