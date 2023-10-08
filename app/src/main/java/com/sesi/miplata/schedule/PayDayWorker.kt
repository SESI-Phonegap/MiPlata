package com.sesi.miplata.schedule

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sesi.miplata.data.entity.GastosRecurrentesV2
import com.sesi.miplata.data.repository.GastosRecurrentesRepository
import com.sesi.miplata.notificaction.MiPlataNotification
import com.sesi.miplata.util.Utils
import com.sesi.miplata.view.main.MenuActivity

class PayDayWorker(private val context: Context, workerParameter: WorkerParameters) :
    CoroutineWorker(context, workerParameter) {

    override suspend fun doWork(): Result {
        Log.i("TaskPayDayWorker", "Init")
        val bills = getCurrentPayments()
        if (bills.isNotEmpty()) {
            val notification = MiPlataNotification()
            val notifyIntent = Intent(context, MenuActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val notifyPendingIntent = PendingIntent.getActivity(
                context,
                0,
                notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            notification.sendPaymentNotification(context, formatMessage(bills), notifyPendingIntent)
        }
        return Result.success()
    }

    private fun getCurrentPayments(): List<GastosRecurrentesV2> {
        val billsRepo = GastosRecurrentesRepository(context)
        return billsRepo.getByDate(Utils.getCurrentDay())
    }

    private fun formatMessage(payments: List<GastosRecurrentesV2>): String {
        val message = StringBuilder()
        payments.forEach { payment ->
            val format = payment.nombre + " $" + payment.monto + "\n"
            message.append(format)
        }
        return message.toString()
    }
}