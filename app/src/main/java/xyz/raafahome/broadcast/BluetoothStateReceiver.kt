package xyz.raafahome.broadcast

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BluetoothStateReceiver(private val onBluetoothStateChanged: (Boolean) -> Unit) :
    BroadcastReceiver() {

    private val TAG = "BluetoothStateReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive: ${intent.action}")
        if (intent.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
            val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
            when (state) {
                BluetoothAdapter.STATE_OFF -> onBluetoothStateChanged(false)
                BluetoothAdapter.STATE_ON -> onBluetoothStateChanged(true)
            }
        }
    }
}