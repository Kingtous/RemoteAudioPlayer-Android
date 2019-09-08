package com.kingtous.remoteaudioplayer;

import android.net.Uri;

import com.kingtous.remoteaudioplayer.model.BluetoothDeviceModel;

public class ConfigHolder {

    // activity result
    public static final int REQUESTCODE_FILESELECT = 1;
    public static final int REQUESTCODE_READ_EXTERNAL = 100;

    public static final String ACT_PAUSE = "pause";
    public static final String ACT_STOP = "stop";
    public static final String ACT_PLAY = "play";

    public static BluetoothDeviceModel btModel;
    public static final String uuid = "fa288726-b927-4c4e-bf4b-f616f386332d";

    public static Uri fileURI;



}
