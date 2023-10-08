package com.sesi.miplata.notificaction

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sesi.miplata.R

class MiPlataNotification {

    fun sendPaymentNotification(context: Context, contentText: String, pendingIntent: PendingIntent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_ID,
                "PaymentNotification",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = context.getString(R.string.notify_title)
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        val logoBitmap =
            BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_round)
        var notification = NotificationCompat.Builder(context, NOTIFICATION_ID)
            .setSmallIcon(R.drawable.ic_launcher_round)
            .setContentTitle(context.getString(R.string.notify_title))
            .setContentText(context.getString(R.string.notify_content))
            .setLargeIcon(logoBitmap)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(contentText)
            )
            .setContentIntent(pendingIntent)
            .build()
        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notify(0, notification)
            }
        }
    }

    companion object {
        const val NOTIFICATION_ID = "NotiId"
    }
}