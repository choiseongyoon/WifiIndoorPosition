package com.example.wearable.wifiindoorposition;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.IBinder;

public class WifiService extends Service {


    private WifiManager mWifiManager;
    private ScheduledFuture<?> scheduleReaderHandle;
    private ScheduledExecutorService mScheduler;
    private WifiData mWifiData;

    private long initialDelay = 0;
    private long periodReader =3000;

    @Override
    public void onCreate() {
        mWifiData = new WifiData();
        mWifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        mScheduler = Executors.newScheduledThreadPool(1);

        scheduleReaderHandle = mScheduler.scheduleAtFixedRate(new ScheduleReader(), initialDelay, periodReader,
                TimeUnit.MILLISECONDS);
    }

    @Override
    public void onDestroy() {
        // stop read thread
        scheduleReaderHandle.cancel(true);
        mScheduler.shutdown();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class ScheduleReader implements Runnable {
        @Override
        public void run() {
            if (mWifiManager.isWifiEnabled()) {
                // get networks
                List<ScanResult> mResults = mWifiManager.getScanResults();
                mWifiData.addNetworks(mResults);
            }
        }
    }
}
