package com.zuluft.mvvm.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.zuluft.mvvm.common.LayoutResId
import com.zuluft.mvvm.viewModels.BaseViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


abstract class BaseActivity<VIEW_STATE, VIEW_MODEL : BaseViewModel<VIEW_STATE>> :
    AppCompatActivity() {

    private lateinit var viewModel: VIEW_MODEL

    private var compositeDisposable: CompositeDisposable? = null

    private var lastViewState: VIEW_STATE? = null

    protected fun getLastViewState(): VIEW_STATE? {
        return lastViewState
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        compositeDisposable = CompositeDisposable()
        val layoutResourceId = javaClass.getAnnotation(LayoutResId::class.java)
        if (layoutResourceId != null) {
            setContentView(layoutResourceId.value)
        }
        renderView(savedInstanceState)
        viewModel = provideViewModel()
        viewModel.getLiveViewState()
            .observe(this,
                Observer {
                    reflectState(it)
                    lastViewState = it
                })
    }

    fun getViewModel(): VIEW_MODEL {
        return viewModel
    }

    abstract fun reflectState(viewState: VIEW_STATE)

    abstract fun renderView(savedInstanceState: Bundle?)

    abstract fun provideViewModel(): VIEW_MODEL

    protected fun registerDisposables(vararg disposables: Disposable) {
        compositeDisposable!!.addAll(*disposables)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (compositeDisposable != null) {
            compositeDisposable!!.dispose()
            compositeDisposable!!.clear()
        }
    }

}