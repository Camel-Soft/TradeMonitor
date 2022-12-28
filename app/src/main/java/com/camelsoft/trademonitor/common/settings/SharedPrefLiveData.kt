package com.camelsoft.trademonitor.common.settings

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.camelsoft.trademonitor._presentation.models.MOffline

abstract class SharedPrefLiveData<T>(
    val sharedPrefs: SharedPreferences,
    val key: String,
    val defValue: T
): LiveData<T>() {

    abstract fun getValue(key: String, defValue: T): T

    private val prefChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == this.key) {
            value = getValue(key, defValue)
        }
    }

    override fun onActive() {
        super.onActive()
        value = getValue(key, defValue)
        sharedPrefs.registerOnSharedPreferenceChangeListener(prefChangeListener)
    }

    override fun onInactive() {
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(prefChangeListener)
        super.onInactive()
    }
}

// *************************************************************************************************

class SharedPrefLiveDataInt(sharedPrefs: SharedPreferences, key: String, defValue: Int) :
    SharedPrefLiveData<Int>(sharedPrefs, key, defValue) {
    override fun getValue(key: String, defValue: Int): Int = sharedPrefs.getInt(key, defValue)
}

class SharedPrefLiveDataString(sharedPrefs: SharedPreferences, key: String, defValue: String) :
    SharedPrefLiveData<String>(sharedPrefs, key, defValue) {
    override fun getValue(key: String, defValue: String): String =
        sharedPrefs.getString(key, defValue).toString()
}

class SharedPrefLiveDataBoolean(sharedPrefs: SharedPreferences, key: String, defValue: Boolean) :
    SharedPrefLiveData<Boolean>(sharedPrefs, key, defValue) {
    override fun getValue(key: String, defValue: Boolean): Boolean = sharedPrefs.getBoolean(key, defValue)
}

class SharedPrefLiveDataFloat(sharedPrefs: SharedPreferences, key: String, defValue: Float) :
    SharedPrefLiveData<Float>(sharedPrefs, key, defValue) {
    override fun getValue(key: String, defValue: Float): Float = sharedPrefs.getFloat(key, defValue)
}

class SharedPrefLiveDataLong(sharedPrefs: SharedPreferences, key: String, defValue: Long) :
    SharedPrefLiveData<Long>(sharedPrefs, key, defValue) {
    override fun getValue(key: String, defValue: Long): Long = sharedPrefs.getLong(key, defValue)
}

class SharedPrefLiveDataSetString(sharedPrefs: SharedPreferences, key: String, defValue: Set<String>) :
    SharedPrefLiveData<Set<String>>(sharedPrefs, key, defValue) {
    override fun getValue(key: String, defValue: Set<String>): Set<String> =
        sharedPrefs.getStringSet(key, defValue) as Set<String>
}

// *************************************************************************************************

fun SharedPreferences.intLiveData(key: String, defValue: Int): SharedPrefLiveData<Int> {
    return SharedPrefLiveDataInt(this, key, defValue)
}

fun SharedPreferences.stringLiveData(key: String, defValue: String): SharedPrefLiveData<String> {
    return SharedPrefLiveDataString(this, key, defValue)
}

fun SharedPreferences.booleanLiveData(key: String, defValue: Boolean): SharedPrefLiveData<Boolean> {
    return SharedPrefLiveDataBoolean(this, key, defValue)
}

fun SharedPreferences.floatLiveData(key: String, defValue: Float): SharedPrefLiveData<Float> {
    return SharedPrefLiveDataFloat(this, key, defValue)
}

fun SharedPreferences.longLiveData(key: String, defValue: Long): SharedPrefLiveData<Long> {
    return SharedPrefLiveDataLong(this, key, defValue)
}

fun SharedPreferences.setStringLiveData(key: String, defValue: Set<String>): SharedPrefLiveData<Set<String>> {
    return SharedPrefLiveDataSetString(this, key, defValue)
}

// *************************************************************************************************

class SharedPrefLiveDataMOffline(private val sharedPrefs: SharedPreferences): LiveData<MOffline>() {

    override fun getValue() = MOffline(
        status = sharedPrefs.getInt("offl_status", 0),
        info = sharedPrefs.getString("offl_info", "") as String,
        stageCurrent = sharedPrefs.getInt("offl_stageCurrent", -1),
        stageTotal = sharedPrefs.getInt("offl_stageTotal", -1),
        stageName = sharedPrefs.getString("offl_stageName", "") as String,
        stagePercent = sharedPrefs.getInt("offl_stagePercent", 0)
    )

    private val prefChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == "offl_status" || key == "offl_info" ||
            key == "offl_stageCurrent" || key == "offl_stageTotal" ||
            key == "offl_stageName" || key == "offl_stagePercent") value = value
    }

    override fun onActive() {
        super.onActive()
        value = value
        sharedPrefs.registerOnSharedPreferenceChangeListener(prefChangeListener)
    }

    override fun onInactive() {
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(prefChangeListener)
        super.onInactive()
    }
}

fun SharedPreferences.mOfflineLiveData() = SharedPrefLiveDataMOffline(sharedPrefs = this)
