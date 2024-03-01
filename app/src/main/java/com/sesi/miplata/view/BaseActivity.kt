package com.sesi.miplata.view

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity: AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
        val configuration = Configuration(newBase.resources.configuration)
        configuration.fontScale = 1.0f
        applyOverrideConfiguration(configuration)
    }

}