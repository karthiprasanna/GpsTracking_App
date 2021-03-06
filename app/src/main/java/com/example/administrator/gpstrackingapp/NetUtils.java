package com.example.administrator.gpstrackingapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by administrator on 1/11/18.
 */

public class NetUtils {
    public static String NO_NETWORK = "oops! it seems like you've lost connectivity..";
    public static String SERVER_ERROR = "Seems like our server is not behaving";
    public static String NETWORK_TIMEOUT = "Oh damn! there goes the network";
    public static String NETWORK_ERROR = "Seems like you are not connected to the Internet";

    public static Boolean isOnline(Context c) {
        ConnectivityManager con = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (con != null) {
            NetworkInfo ni = con.getActiveNetworkInfo();
            if (ni != null && ni.isAvailable() && ni.isConnected()) {

                return true;
            }
        }
        return false;
    }
}