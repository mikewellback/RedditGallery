package com.mikewellback.redditgallery.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class NetworkUtils {
    companion object {
        fun hasConnection(context: Context): Boolean {
            (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?)?.also {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    it.activeNetworkInfo?.also { net ->
                        return net.isConnected
                    }
                } else {
                    it.getNetworkCapabilities(it.activeNetwork)?.also { cap ->
                        return cap.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                                || cap.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    }
                }
            }
            return false
        }
    }
}