package com.zuluft.mvvm.preferences

import io.reactivex.Observable

interface Preferences {
    fun add(key: String, value: Int): Preferences

    fun add(key: String, value: String): Preferences

    fun add(key: String, value: Boolean): Preferences

    fun add(key: String, value: Float): Preferences

    fun remove(key: String): Preferences

    fun getObservable(key: String, defaultValue: Int): Observable<Int>

    fun getObservable(key: String, defaultValue: String): Observable<String>

    fun getObservable(key: String, defaultValue: Boolean): Observable<Boolean>

    fun getObservable(key: String, defaultValue: Float): Observable<Float>

    operator fun get(key: String, defaultValue: Int): Int

    operator fun get(key: String, defaultValue: String): String

    operator fun get(key: String, defaultValue: Boolean): Boolean

    operator fun get(key: String, defaultValue: Float): Float

    fun commit()

    fun commitAsync()

    fun clearAllData()
}