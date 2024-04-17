package xyz.raafahome

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import xyz.raafahome.broadcast.BluetoothStateReceiver
import xyz.raafahome.broadcast.NetworkConnectivityCallback
import xyz.raafahome.state.BluetoothState
import xyz.raafahome.state.NetworkState

class PrinterApplication : Application() {

    private val TAG = "PrinterApplication"
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkConnectivityCallback: NetworkConnectivityCallback
    private lateinit var bluetoothStateReceiver: BluetoothStateReceiver

    override fun onCreate() {
        super.onCreate()

        bluetoothStateReceiver = BluetoothStateReceiver { isEnabled ->
            Log.d(TAG, "onCreate Bluetooth State Receiver: $isEnabled")
            if (isEnabled) {
                handleBluetoothEnabled()
            } else {
                handleBluetoothDisabled()
            }
        }
        val filterBluetooth = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)

        registerReceiver(bluetoothStateReceiver, filterBluetooth)

        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        networkConnectivityCallback = NetworkConnectivityCallback(
            onNetworkAvailable = { handleNetworkConnected() },
            onNetworkLost = { handleNetworkDisconnected() }
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkConnectivityCallback)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        unregisterReceiver(bluetoothStateReceiver)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.unregisterNetworkCallback(networkConnectivityCallback)
        }
    }

    private fun handleBluetoothEnabled() {
        BluetoothState.updateBluetoothStatus(BluetoothState.BluetoothStatus.ENABLED)
        Log.d(TAG, "handleBluetoothEnabled: ")
    }

    private fun handleBluetoothDisabled() {
        BluetoothState.updateBluetoothStatus(BluetoothState.BluetoothStatus.DISABLED)
        Log.d(TAG, "handleBluetoothDisabled: ")
    }

    private fun handleNetworkConnected() {
        NetworkState.updateNetworkStatus(NetworkState.NetworkStatus.CONNECTED)
        Log.d(TAG, "handleNetworkConnected: ")
    }

    private fun handleNetworkDisconnected() {
        NetworkState.updateNetworkStatus(NetworkState.NetworkStatus.DISCONNECTED)
        Log.d(TAG, "handleNetworkDisconnected: ")
    }
}