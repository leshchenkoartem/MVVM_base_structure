package com.zuluft.mvvm.usecases

abstract class BaseUseCase<REPOSITORY, ARG_TYPE, RETURN_TYPE>
constructor(protected val repository: REPOSITORY) {

    abstract fun start(arg: ARG_TYPE? = null): RETURN_TYPE
}