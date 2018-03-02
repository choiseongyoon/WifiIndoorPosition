package com.example.wearable.wifiindoorposition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.net.wifi.ScanResult;


public class WifiData  {
    private List<WifiDataNetwork> mNetworks;

    public WifiData() {
        mNetworks = new ArrayList<>();
    }

    public void addNetworks(List<ScanResult> results) {
        mNetworks.clear();
        for (ScanResult result : results) {
            mNetworks.add(new WifiDataNetwork(result));
        }
        Collections.sort(mNetworks);
    }

}
