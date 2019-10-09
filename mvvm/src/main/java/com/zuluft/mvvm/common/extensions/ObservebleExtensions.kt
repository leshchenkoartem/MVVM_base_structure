package com.zuluft.mvvm.common.extensions

import android.os.Handler
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers

const val DEFAULT_POST_DELAY = 500L

fun <T : Any> Observable<T>.async(): Observable<T> {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T : Any> T.just(): Observable<T> {
    return Observable.just(this)
}

fun postDelayed(duration: Long = DEFAULT_POST_DELAY): Observable<Boolean> {
    return Observable.create { emitter ->
        val handler = Handler()

        val action = {
            if (!emitter.isDisposed) {
                emitter.onNext(true)
                emitter.onComplete()
            }
        }

        handler.postDelayed(action, duration)
        emitter.setDisposable(Disposables.fromAction { handler.removeCallbacks(action) })
    }
}