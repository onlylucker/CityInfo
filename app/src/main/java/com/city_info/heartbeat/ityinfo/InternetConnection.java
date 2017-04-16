package com.city_info.heartbeat.ityinfo;


import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;

/**
 * Класс для проверки на подключение к интернету
 */
public class InternetConnection {

    public static boolean checkConnection(@NonNull Context context){

        return ((ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;

    }
}
