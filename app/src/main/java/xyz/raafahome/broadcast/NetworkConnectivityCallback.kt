package xyz.raafahome.broadcast

import android.net.ConnectivityManager
import android.net.Network

class NetworkConnectivityCallback(
    private val onNetworkAvailable: () -> Unit,
    private val onNetworkLost: () -> Unit
) : ConnectivityManager.NetworkCallback() {

    override fun onLost(network: Network) {
        super.onLost(network)
        onNetworkLost()
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        onNetworkAvailable()
    }
}