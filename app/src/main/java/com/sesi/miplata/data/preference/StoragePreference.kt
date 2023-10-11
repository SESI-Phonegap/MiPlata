package com.sesi.miplata.data.preference

import android.content.Context

object StoragePreference {

    const val TAG = "MiPlata";
    const val FIRST_TIME = "firstTime";

    fun setData(prefName: String, any: Any, context: Context) {
        val preference = context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
        val editor = preference.edit()

        if (any is Boolean) {
            editor.putBoolean(prefName, any)
            editor.apply()
        }
    }

    fun isFirstTime(context: Context): Boolean {
        val preference = context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
        return preference.getBoolean(FIRST_TIME, true)
    }
}