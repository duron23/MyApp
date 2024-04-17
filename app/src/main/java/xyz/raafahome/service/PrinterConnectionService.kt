package xyz.raafahome.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xyz.raafahome.MainActivity
import xyz.raafahome.R
import xyz.raafahome.broadcast.StopServiceReceiver
import xyz.raafahome.state.BTPrinterState
import xyz.raafahome.state.ServiceState
import kotlin.coroutines.CoroutineContext

class PrinterConnectionService(override val coroutineContext: CoroutineContext) : Service() , CoroutineScope {

    companion object {
        private const val NOTIFICATION_ID = 1
    }

    private val TAG = "PrinterConnection"
    private lateinit var notificationManager: NotificationManager

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        launch {
            // Switch to IO dispatcher for network or database operations
            withContext(Dispatchers.IO) {
                // Long-running operation here
            }
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //return super.onStartCommand(intent, flags, startId)
        startForegroundService()
        return START_STICKY
    }


    private fun btPrinterConnected() {
        BTPrinterState.updatePrinterStatus(BTPrinterState.PrinterStatus.CONNECTED)
        ServiceState.setHostAddress("XX:XX:XX:XX:XX")
        ServiceState.setConnectionType(ServiceState.ConnectionType.BLUETOOTH)
        updateNotification(msg = "Printer Connected: " + ServiceState.hostAddress.value)
    }

    private fun btPrinterDisconnected() {
        BTPrinterState.updatePrinterStatus(BTPrinterState.PrinterStatus.DISCONNECTED)
        ServiceState.setHostAddress("")
        ServiceState.setConnectionType(ServiceState.ConnectionType.UNDEFINED)
        ServiceState.setBusinessType(ServiceState.BusinessType.UNDEFINED)
        updateNotification(msg = "Printer Disconnected")
    }

    private fun startForegroundService() {
        val notificationChannelId = "Printer Service Channel"
        val channelName = "Printer Connection Service"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(
                notificationChannelId,
                channelName, NotificationManager.IMPORTANCE_NONE
            )
            notificationManager.createNotificationChannel(chan)
        }

        val stopIntent = Intent(this, StopServiceReceiver::class.java)
        val stopPendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val mainActivityPendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, notificationChannelId)
        val notification: Notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notificationBuilder.setOngoing(true)
                .setContentTitle("Printer Connection Service")
                .setContentText("Service Started")
                .setSmallIcon(R.drawable.baseline_print_24)
                .setContentIntent(mainActivityPendingIntent)
                .addAction(R.drawable.baseline_adjust_24, "Stop", stopPendingIntent)
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build()
        } else {
            notificationBuilder.setOngoing(true)
                .setContentTitle("Printer Connection Service")
                .setContentText("Service Started")
                .setSmallIcon(R.drawable.baseline_print_24)
                .setContentIntent(mainActivityPendingIntent)
                .addAction(R.drawable.baseline_adjust_24, "Stop", stopPendingIntent)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build()
        }


        startForeground(NOTIFICATION_ID, notification)
        ServiceState.setPrinterServiceRunning(true)
        btPrinterConnected()
    }

    override fun onDestroy() {
        super.onDestroy()
        btPrinterDisconnected()
        ServiceState.setPrinterServiceRunning(false)
        notificationManager.cancel(NOTIFICATION_ID)
        Log.d(TAG, "onDestroy: ")
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
    }

    private fun updateNotification(msg: String) {
        val notificationChannelId = "Printer Service Channel"
        val stopIntent = Intent(this, StopServiceReceiver::class.java)
        val stopPendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val mainActivityPendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notificationBuilder = NotificationCompat.Builder(this, notificationChannelId)
        val notification: Notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notificationBuilder.setOngoing(true)
                .setContentTitle("Printer Connection Service")
                .setContentText("LastPrint: ${ServiceState.lastPrint.value}")
                .setColor(Color.GREEN)
                .setSubText(msg)
                .setColorized(true)
                .setSmallIcon(R.drawable.baseline_print_24)
                .setBadgeIconType(R.drawable.baseline_print_24)
                .setContentIntent(mainActivityPendingIntent)
                .addAction(R.drawable.baseline_adjust_24, "Stop", stopPendingIntent)
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build()
        } else {
            notificationBuilder.setOngoing(true)
                .setContentTitle("Printer Connection Service")
                .setContentText("LastPrint: ${ServiceState.lastPrint.value}")
                .setColor(Color.GREEN)
                .setSubText(msg)
                .setColorized(true)
                .setSmallIcon(R.drawable.baseline_print_24)
                .setBadgeIconType(R.drawable.baseline_print_24)
                .setContentIntent(mainActivityPendingIntent)
                .addAction(R.drawable.baseline_adjust_24, "Stop", stopPendingIntent)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build()
        }

        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}