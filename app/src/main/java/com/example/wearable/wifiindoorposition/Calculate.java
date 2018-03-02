package com.example.wearable.wifiindoorposition;

import android.support.v7.app.AppCompatActivity;

import static java.lang.Math.pow;


public class Calculate extends AppCompatActivity {

    static class KalmanFilter {

        private double Q = 0.00001;
        private double R = 0.001;
        private double X = 0, P = 1, K;

        //첫번째값을 입력받아 초기화 한다. 예전값들을 계산해서 현재값에 적용해야 하므로 반드시 하나이상의 값이 필요하므로~

        KalmanFilter(double initValue) {
            X = initValue;

        }

        //예전값들을 공식으로 계산한다

        private void measurementUpdate() {
            K = (P + Q) / (P + Q + R);
            P = R * (P + Q) / (R + P + Q);

        }

        //현재값을 받아 계산된 공식을 적용하고 반환한다
        public double update(double measurement) {
            measurementUpdate();
            X = X + (measurement - X) * K;
            return X;
        }
    }
    public final double  kFilteringFactor = 0.1;
    public float getrollingRssi(int level)
    {
        float rollingRssi =0;
        rollingRssi= (float) ((level * kFilteringFactor) + (rollingRssi * (1.0 - kFilteringFactor)));
        return rollingRssi;
    }
    public double calculateAccuracyWithRSSI(double rssi) {
        if (rssi == 0) {
            return -1.0;
        }

        double txPower = -70;
        double ratio = rssi*1.0/txPower;
        if (ratio < 1.0) {
            return pow(ratio,10);
        }
        else {
            double accuracy =  (0.89976) * pow(ratio,7.7095) + 0.111;
            return accuracy;
        }
    }
    public double[] trilateration (float[] P1, float[] P2, float[] P3, double d1, double d2, double d3) {

        double S = (pow(P3[0], 2.) - pow(P2[0], 2.) + pow(P3[1], 2.) - pow(P2[1], 2.) + pow(d2, 2.) - pow(d3, 2.)) / 2.0;
        double T = (pow(P1[0], 2.) - pow(P2[0], 2.) + pow(P1[1], 2.) - pow(P2[1], 2.) + pow(d2, 2.) - pow(d1, 2.)) / 2.0;
        double y = ((T * (P2[0] - P3[0])) - (S * (P2[0] - P1[0]))) / (((P1[1] - P2[1]) * (P2[0] - P3[0])) - ((P3[1] - P2[1]) * (P2[0] - P1[0])));
        double x = ((y * (P1[1] - P2[1])) - T) / (P2[0] - P1[0]);
        double result[]={x,y};
        return result;

    }

    public double getDistance (int frequency, int level) {
        double exp = (27.55 - (20 * Math.log10(frequency)) + Math.abs(level)) / 20.0;
        double distance = pow(10.0, exp);
        return distance;
    }
    public double getDistance_float (float frequency, float level) {
        double exp = (27.55 - (20 * Math.log10(frequency)) + Math.abs(level)) / 20.0;
        double distance = pow(10.0, exp);
        return distance;
    }

    public float KalmanFileter(double num){
        KalmanFilter mKalmanAccF = new KalmanFilter(0.0f);
        float F = (float) num;
        float filteredF = (float) mKalmanAccF.update(F);
        return filteredF;
    }


    public double[] position_calculate(float[] P1, float[] P2, float[] P3, double d1, double d2, double d3) {
        double[] ex   = new double[2];
        double[] ey   = new double[2];
        double[] p3p1 = new double[2];

/*

        for (int i = 0; i < P1.length; i++) {
            t1   = P2[i];
            t2   = P1[i];
            t    = t1 - t2;
            temp += (t*t);
        }
        d = Math.sqrt(temp);
        for (int i = 0; i < P1.length; i++) {
            t1    = P2[i];
            t2    = P1[i];
            exx   = (t1 - t2)/(Math.sqrt(temp));
            ex[i] = exx;
        }
        for (int i = 0; i < P3.length; i++) {
            t1      = P3[i];
            t2      = P1[i];
            t3      = t1 - t2;
            p3p1[i] = t3;
        }
        for (int i = 0; i < ex.length; i++) {
            t1 = ex[i];
            t2 = p3p1[i];
            ival += (t1*t2);
        }
        for (int  i = 0; i < P3.length; i++) {
            t1 = P3[i];
            t2 = P1[i];
            t3 = ex[i] * ival;
            t  = t1 - t2 -t3;
            p3p1i += (t*t);
        }
        for (int i = 0; i < P3.length; i++) {
            t1 = P3[i];
            t2 = P1[i];
            t3 = ex[i] * ival;
            eyy = (t1 - t2 - t3)/Math.sqrt(p3p1i);
            ey[i] = eyy;
        }
        for (int i = 0; i < ey.length; i++) {
            t1 = ey[i];
            t2 = p3p1[i];
            jval += (t1*t2);
        }

        xval = (Math.pow(d1, 2) - Math.pow(d2, 2) + Math.pow(d, 2))/(2*d);
        yval = ((Math.pow(d1, 2) - Math.pow(d3, 2) + Math.pow(ival, 2) + Math.pow(jval, 2))/(2*jval)) - ((ival/jval)*xval);

        t1 =   P1[0];
        t2 = ex[0] * xval;
        t3 = ey[0] * yval;
        triptx = t1 + t2 + t3;

        t1 = P1[1];
        t2 = ex[1] * xval;
        t3 = ey[1] * yval;
        tripty = t1 + t2 + t3;
        //     String ans = "X: "+(triptx) + "\n" + "Y: " + (tripty);
        //   Toast.makeText(getApplication(),ans,Toast.LENGTH_LONG).show();


*/


        double p2p1distance = Math.pow(Math.pow(P2[0] - P1[0] , 2) + Math.pow(P2[1] - P1[1] , 2) , 0.5);
        double exx = (P2[0] - P1[0])/p2p1distance;
        double exy = (P2[1] - P1[1])/p2p1distance;
        double auxx = (P3[0] - P1[0]);
        double auxy = (P3[1] - P1[1]);
        double i = exx * auxx + exy * auxy;
        double aux2x = P3[0] - P1[0] - (i * exx);
        double aux2y = P3[1] - P1[1] - (i * exy);
        double eyx = aux2x / (Math.pow(Math.pow(aux2x,2) + Math.pow(aux2y,2) , 0.5)) ;
        double eyy = aux2y / (Math.pow(Math.pow(aux2x,2) + Math.pow(aux2y,2) , 0.5)) ;
        double j = eyx * auxx + eyy * auxy;
        double x = (Math.pow(d1,2) - Math.pow(d2,2) + Math.pow(p2p1distance,2))/ (2 * p2p1distance);
        double y = (Math.pow(d1,2) - Math.pow(d3,2) + Math.pow(i,2) + Math.pow(j,2))/(2*j) - i*x/j;
        double triptx = (P1[0] + x * exx + y * eyx);
        double tripty = (P1[1] + x * exy + y * eyy);


        double result[]={triptx,tripty};
        return result;
        //return  triptx;
    }


}