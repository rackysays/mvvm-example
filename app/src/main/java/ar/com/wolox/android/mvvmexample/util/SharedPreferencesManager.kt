package ar.com.wolox.android.mvvmexample.util

import android.content.SharedPreferences
import ar.com.wolox.android.mvvmexample.di.scope.ApplicationScope
import javax.inject.Inject

@ApplicationScope
class SharedPreferencesManager @Inject constructor(private val sharedPreferences: SharedPreferences){

    fun store(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun store(key: String, value: Int?) {
        sharedPreferences.edit().putInt(key, value!!).apply()
    }

    fun store(key: String, value: Float?) {
        sharedPreferences.edit().putFloat(key, value!!).apply()
    }

    fun store(key: String, value: Boolean?) {
        sharedPreferences.edit().putBoolean(key, value!!).apply()
    }

    fun store(key: String, value: Long?) {
        sharedPreferences.edit().putLong(key, value!!).apply()
    }

    operator fun get(key: String, defValue: String): String? {
        return sharedPreferences.getString(key, defValue)
    }

    operator fun get(key: String, defValue: Int): Int {
        return sharedPreferences.getInt(key, defValue)
    }

    operator fun get(key: String, defValue: Float): Float {
        return sharedPreferences.getFloat(key, defValue)
    }

    operator fun get(key: String, defValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defValue)
    }

    operator fun get(key: String, defValue: Long): Long {
        return sharedPreferences.getLong(key, defValue)
    }

    fun clearKey(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    fun keyExists(key: String): Boolean {
        return sharedPreferences.contains(key)
    }
}