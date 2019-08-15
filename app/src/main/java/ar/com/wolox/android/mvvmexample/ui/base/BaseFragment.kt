package ar.com.wolox.android.mvvmexample.ui.base

import android.os.Bundle
import android.view.View
import dagger.android.support.DaggerFragment

abstract class BaseFragment: DaggerFragment(){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setListener()
    }

    open fun setListener(){}

    abstract fun init()
    abstract fun layout(): Int
}