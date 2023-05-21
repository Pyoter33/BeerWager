package com.example.beerwager.utils

import android.Manifest
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.example.beerwager.MainActivity
import com.example.beerwager.R
import com.example.beerwager.domain.models.WagerFilter

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val wagerId = intent.extras?.getLong(NotificationHelper.EXTRA_ID) ?: return
        val title = intent.extras?.getString(NotificationHelper.EXTRA_TITLE) ?: return

        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(NOTIFICATION_ID, createNotification(wagerId, title, context))
        }
    }

    private fun createNotification(wagerId: Long, wagerTitle: String, context: Context): Notification {
        val taskDetailIntent = Intent(
            Intent.ACTION_VIEW,
            "https://beerwager.com/wagersCreate/wagerId=${wagerId}/category=${WagerFilter.UPCOMING}".toUri(),
            context,
            MainActivity::class.java
        )

        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(taskDetailIntent)
            getPendingIntent(REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(context.getString(R.string.text_notification_title))
            .setContentText(wagerTitle)
            .setSmallIcon(R.drawable.baseline_notifications_active_24)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }

    companion object {
        private const val REQUEST_CODE = 1
        private const val CHANNEL_NAME = "WagerChannel"
        private const val CHANNEL_ID = "WagerChannelId"
        private const val NOTIFICATION_ID = 0
    }
}

