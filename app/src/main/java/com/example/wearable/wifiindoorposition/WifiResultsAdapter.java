package com.example.wearable.wifiindoorposition;

import android.net.wifi.ScanResult;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;

public class WifiResultsAdapter extends RecyclerView.Adapter<WifiResultsAdapter.ViewHolder> {
    private List<ScanResult> results = new ArrayList<>();

    @Override
    public WifiResultsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wifi_result, parent, false);
        // set the view's size, margins, paddings and layout parameters
        WifiResultsAdapter.ViewHolder vh = new WifiResultsAdapter.ViewHolder(linearLayout);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bssid.setText("MAC: "+results.get(position).BSSID);
        holder.ssid.setText("SSID: "+results.get(position).SSID);
        holder.level.setText(String.valueOf("RSSI(dB):"+ results.get(position).level));
        holder.distance.setText(String.valueOf("Distance(m):"+getDistance(results.get(position).frequency,results.get(position).level)));
    }

    private String getDistance(int frequency, int level) {
        double exp = (27.55 - (20 * Math.log10(frequency)) + Math.abs(level)) / 20.0;
        double distance = pow(10.0, exp);
        String finalDistance = String.format("%.4f", distance);
        return finalDistance;
    }



    @Override
    public int getItemCount() {
        return results.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView bssid, ssid, level, distance;

        public ViewHolder(LinearLayout v) {
            super(v);
            bssid = v.findViewById(R.id.wifi_bssid);
            ssid = v.findViewById(R.id.wifi_ssid);
            level = v.findViewById(R.id.wifi_level);
            distance = v.findViewById(R.id.wifi_distance);
        }
    }

    public List<ScanResult> getResults() {
        return results;
    }

    public void setResults(List<ScanResult> results) {
        this.results = results;
    }
}
