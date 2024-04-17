package xyz.raafahome.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import xyz.raafahome.service.PrinterConnectionService

class StopServiceReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val serviceIntent = Intent(context, PrinterConnectionService::class.java)
        context.stopService(serviceIntent)
    }
}