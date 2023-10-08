package com.sesi.miplata.schedule

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sesi.miplata.R
import com.sesi.miplata.notificaction.MiPlataNotification
import com.sesi.miplata.view.main.RegistroGastoIngresoActivity

class DailyWorker(private val context: Context, workerParameter: WorkerParameters) :
    CoroutineWorker(context, workerParameter) {

    override suspend fun doWork(): Result {
        Log.i("DayliWorker", "init DayliWorker")
        val notification = MiPlataNotification()
        val notifyIntent = Intent(context, RegistroGastoIngresoActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val notifyPendingIntent = PendingIntent.getActivity(
            context,
            0,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        notification.sendPaymentNotification(context, context.getString(R.string.dialog_dayli_message), notifyPendingIntent)
        return Result.success()
    }
}