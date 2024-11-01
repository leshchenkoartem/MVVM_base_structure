package com.zuluft.mvvm.preferences

import android.content.SharedPreferences
import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Observable


class PreferencesImpl
constructor(private val sharedPreferences: SharedPreferences) : Preferences {

    private val rxSharedPreferences: RxSharedPreferences = RxSharedPreferences.create(sharedPreferences)
    private val editor: SharedPreferences.Editor = this.sharedPreferences.edit()

    override fun add(key: String, value: Int): Preferences {
        editor.putInt(key, value)
        return this
    }

    override fun add(key: String, value: String): Preferences {
        editor.putString(key, value)
        return this
    }

    override fun add(key: String, value: Boolean): Preferences {
        editor.putBoolean(key, value)
        return this
    }

    override fun add(key: String, value: Float): Preferences {
        editor.putFloat(key, value)
        return this
    }

    override fun remove(key: String): Preferences {
        editor.remove(key)
        return this
    }

    override fun getObservable(key: String, defaultValue: Int): Observable<Int> {
        return rxSharedPreferences.getInteger(key, defaultValue).asObservable()
    }

    override fun getObservable(key: String, defaultValue: String): Observable<String> {
        return rxSharedPreferences.getString(key, defaultValue).asObservable()
    }

    override fun getObservable(key: String, defaultValue: Boolean): Observable<Boolean> {
        return rxSharedPreferences.getBoolean(key, defaultValue).asObservable()
    }

    override fun getObservable(key: String, defaultValue: Float): Observable<Float> {
        return rxSharedPreferences.getFloat(key, defaultValue).asObservable()
    }

    override fun get(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    override fun get(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue).toString()
    }

    override fun get(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    override fun get(key: String, defaultValue: Float): Float {
        return sharedPreferences.getFloat(key, defaultValue)
    }

    override fun commit() {
        editor.commit()
    }

    override fun commitAsync() {
        editor.apply()
    }

    override fun clearAllData() {
        editor.clear().commit()
    }
}