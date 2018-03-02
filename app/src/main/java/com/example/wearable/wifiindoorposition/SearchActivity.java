package com.example.wearable.wifiindoorposition;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class SearchActivity extends Calculate  implements View.OnClickListener, RecyclerItemClickListener.OnItemClickListener{
    private RecyclerView rvWifis;
    private RecyclerView.LayoutManager layoutManager;
    public WifiManager mainWifi;
    private WifiListReceiver receiverWifi;
    private final Handler handler = new Handler();
    private Button btnRefrsh,button;
    public List<ScanResult> results = new ArrayList<>();
    private WifiResultsAdapter wifiResultsAdapter = new WifiResultsAdapter();
    private boolean wifiWasEnabled;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initUI();
        mainWifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        receiverWifi = new WifiListReceiver();

        wifiWasEnabled = mainWifi.isWifiEnabled();
        if (!mainWifi.isWifiEnabled()) {
            mainWifi.setWifiEnabled(true);
        }
        layoutManager = new LinearLayoutManager(this);
        rvWifis.setLayoutManager(layoutManager);
        rvWifis.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        rvWifis.setItemAnimator(new DefaultItemAnimator());
        rvWifis.setAdapter(wifiResultsAdapter);
        rvWifis.addOnItemTouchListener(new RecyclerItemClickListener(this,rvWifis, this));
    }
    public void refresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainWifi.startScan();
            }
        }, 1000);
    }

    @Override
    protected void onResume() {
        registerReceiver(receiverWifi, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        refresh();
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiverWifi);
        super.onPause();
    }
    private void initUI() {
        rvWifis = findViewById(R.id.rv_wifis);
      //  btnRefrsh = findViewById(R.id.btn_wifi_refresh);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, InActivity.class);
                startActivity(intent);
            }
        });
/*
        btnRefrsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
                Toast.makeText(getApplication(),"refresh",Toast.LENGTH_LONG).show();
            }
        });
  */
    }

    @Override
    public void onItemClick(View view, int position) {
    }



    @Override
    public void onLongClick(View view, int position) {

    }

    @Override
    public void onClick(View view) {
    }

    class WifiListReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            results = mainWifi.getScanResults();
            Collections.sort(results, new Comparator<ScanResult>() {
                @Override
                public int compare(ScanResult scanResult, ScanResult scanResult2) {
                    if (scanResult.level > scanResult2.level) {
                        return -1;
                    } else if (scanResult.level < scanResult2.level) {
                        return 1;
                    }
                    return 0;
                }
            });
            wifiResultsAdapter.setResults(results);
            wifiResultsAdapter.notifyDataSetChanged();
            refresh();

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!wifiWasEnabled) {
            mainWifi.setWifiEnabled(false);
        }
    }
}
