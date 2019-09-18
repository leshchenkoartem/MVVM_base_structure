package com.zuluft.mvvm.common


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class LayoutResId(
    val value: Int
)