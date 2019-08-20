package ar.com.wolox.android.mvvmexample.ui.base

import android.os.Bundle
import android.view.View
import dagger.android.support.DaggerFragment

abstract class BaseFragment: DaggerFragment(){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeLiveData()
    }

    /**
     * Sets the listeners for the views of the fragment.
     * Override if needed.
     */
    open fun setListener(){}

    /**
     * For setting Live data observations, must be initialize viewModel first, in order to be reactive
     * Override if needed.
     */
    open fun observeLiveData(){}

    /**
     * Layout resource to be added in Fragment
     */
    abstract fun layout(): Int

}