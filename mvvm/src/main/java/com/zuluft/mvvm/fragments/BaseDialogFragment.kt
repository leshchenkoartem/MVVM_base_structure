package com.zuluft.mvvm.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.Guideline
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.zuluft.mvvm.R
import com.zuluft.mvvm.common.LayoutResId
import com.zuluft.mvvm.viewModels.BaseViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseDialogFragment<VIEW_STATE, VIEW_MODEL : BaseViewModel<VIEW_STATE>>
    : DialogFragment() {


    private var compositeDisposable: CompositeDisposable? = null

    private val dialogsList: ArrayList<Dialog> = ArrayList()

    private lateinit var viewModel: VIEW_MODEL

    private var lastViewState: VIEW_STATE? = null

    protected fun getLastViewState(): VIEW_STATE? {
        return lastViewState
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        compositeDisposable = CompositeDisposable()
        val rootView =
            inflater
                .inflate(
                    R.layout.layout_base_dialog_fragment,
                    container, false
                ) as ViewGroup
        val widthPercent = getWidthPercent()
        val startPercent = (1f - widthPercent) / 2f
        val endPercent = 1f - startPercent
        val startGuideline = rootView.findViewById<Guideline>(R.id.startGuideline)
        val endGuideline = rootView.findViewById<Guideline>(R.id.endGuideline)
        startGuideline.setGuidelinePercent(startPercent)
        endGuideline.setGuidelinePercent(endPercent)
        val contentViewGroup = rootView.findViewById<ViewGroup>(R.id.flDialogContent)
        contentViewGroup.addView(createView(inflater, contentViewGroup))
        val window = dialog?.window
        if (window != null) {
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            setStyle(STYLE_NO_FRAME, android.R.style.Theme)
        }
        if (isCancelable) {
            rootView.setOnTouchListener { _, _ ->
                dismissAllowingStateLoss()
                true
            }
        }
        return rootView
    }

    protected fun registerDialog(dialog: Dialog) {
        dialogsList.add(dialog)
    }

    protected fun isDialogShowing(): Boolean {
        for (dialog in dialogsList) {
            if (dialog.isShowing) {
                return true
            }
        }
        return false
    }

    protected fun dismissAndClearDialogs() {
        for (dialog in dialogsList) {
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }
        dialogsList.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    fun getViewModel(): VIEW_MODEL {
        return viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderView(savedInstanceState)

    }

    protected fun changeKeyboardAppearance(resize: Boolean) {
        (context as Activity).window
            .setSoftInputMode(
                when (resize) {
                    false -> WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
                    else -> WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                }
            )
    }

    protected fun closeKeyboard() {
        val activity = context as Activity
        val view = activity.currentFocus
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    protected fun registerDisposables(vararg disposables: Disposable) {
        compositeDisposable!!.addAll(*disposables)
    }

    abstract fun reflectState(viewState: VIEW_STATE)

    abstract fun renderView(savedInstanceState: Bundle?)

    abstract fun provideViewModel(): VIEW_MODEL

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = provideViewModel()
        viewModel.getLiveViewState()
            .observe(viewLifecycleOwner,
                Observer {
                    reflectState(it)
                    lastViewState = it
                })
    }

    abstract fun getWidthPercent(): Float

    protected open fun createView(inflater: LayoutInflater, container: ViewGroup): View? {
        var view: View? = null
        val layoutResourceId = javaClass.getAnnotation(LayoutResId::class.java)
        if (layoutResourceId != null) {
            view = inflater.inflate(layoutResourceId.value, container, false)
        }
        return view
    }


    override fun onDestroyView() {
        dismissAndClearDialogs()
        if (compositeDisposable != null) {
            compositeDisposable!!.dispose()
            compositeDisposable!!.clear()
        }
        super.onDestroyView()
    }

}