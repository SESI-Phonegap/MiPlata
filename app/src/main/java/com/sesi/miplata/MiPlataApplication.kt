package com.sesi.miplata

import android.app.Application
import com.google.android.gms.ads.MobileAds

class MiPlataApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(
            this
        ) { }
    }
}