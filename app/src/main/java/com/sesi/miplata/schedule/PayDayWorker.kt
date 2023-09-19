package com.sesi.miplata.schedule

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class PayDayWorker(context: Context, workerParameter: WorkerParameters): Worker(context, workerParameter) {
    override fun doWork(): Result {

       return Result.success()
    }
}