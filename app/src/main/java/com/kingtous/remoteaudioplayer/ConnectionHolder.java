package com.kingtous.remoteaudioplayer;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.widget.Toast;

public class ConnectionHolder {

    // MainActivity will initialize a static ConnectionHolder

    private Context context;

    //Bluetooth
    public static BluetoothManager bluetoothManager;
    public static BluetoothAdapter bluetoothAdapter;


    ConnectionHolder(Context context){
        this.context = context;
        bluetoothManager = (BluetoothManager) context.getSystemService(Activity.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(context, context.getString(R.string.msg_unsupported_bluetooth), Toast.LENGTH_SHORT).show();
        }
    }






}
