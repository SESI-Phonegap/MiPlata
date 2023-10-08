package com.sesi.miplata.schedule

import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class JobManager {
    fun createNotificationJob(context: Context) {
        val work = PeriodicWorkRequestBuilder<PayDayWorker>(12, TimeUnit.HOURS).build()
        val workManager = WorkManager.getInstance(context)
        workManager.enqueue(work)
    }

    fun createDailyNotificationJob(context: Context) {
        val work = PeriodicWorkRequestBuilder<DailyWorker>(8, TimeUnit.HOURS).build()
        val workManager = WorkManager.getInstance(context)
        workManager.enqueue(work)
    }
}