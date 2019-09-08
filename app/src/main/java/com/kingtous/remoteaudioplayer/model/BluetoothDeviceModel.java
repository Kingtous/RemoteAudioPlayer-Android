package com.kingtous.remoteaudioplayer.model;

public class BluetoothDeviceModel {

    String name;
    String mac;

    public String getName() {
        return name;
    }

    public String getMac() {
        return mac;
    }

    public BluetoothDeviceModel(String name, String mac) {
        this.name = name;
        this.mac = mac;
    }
}
