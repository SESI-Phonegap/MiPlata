package com.sesi.miplata.schedule

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.sesi.miplata.data.entity.GastosRecurrentes
import com.sesi.miplata.data.repository.GastosRecurrentesRepository
import com.sesi.miplata.notificaction.MiPlataNotification
import com.sesi.miplata.util.Utils

class PayDayWorker(private val context: Context, workerParameter: WorkerParameters) :
    Worker(context, workerParameter) {

    override fun doWork(): Result {
        val bills = getCurrentPayments()
        if (bills.isNotEmpty()) {
            val notification = MiPlataNotification()
            notification.sendPaymentNotification(context, formatMessage(bills))
        }
        return Result.success()
    }

    private fun getCurrentPayments(): List<GastosRecurrentes> {
        val billsRepo = GastosRecurrentesRepository(context)
        return billsRepo.getByDate(Utils.getCurrentDay())
    }

    private fun formatMessage(payments: List<GastosRecurrentes>): String {
        val message = StringBuilder()
        payments.forEach { payment ->
            val format = payment.nombre + " $" + payment.monto + "\n"
            message.append(format)
        }
        return message.toString()
    }
}