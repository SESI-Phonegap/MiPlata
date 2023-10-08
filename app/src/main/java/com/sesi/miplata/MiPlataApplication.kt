package com.sesi.miplata

import android.app.Application
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.sesi.miplata.schedule.DailyWorker
import com.sesi.miplata.schedule.PayDayWorker
import java.util.concurrent.TimeUnit

class MiPlataApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}