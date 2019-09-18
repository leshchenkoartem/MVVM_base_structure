package com.zuluft.mvvm.actions

interface ViewStateAction<VIEW_STATE> {
    fun newState(oldState: VIEW_STATE): VIEW_STATE
}