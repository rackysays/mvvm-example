package ar.com.wolox.android.mvvmexample.ui.base

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import ar.com.wolox.android.mvvmexample.util.ConnectionLiveData
import dagger.android.support.DaggerFragment

abstract class BaseFragment: DaggerFragment(){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setListener()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeLiveData()

        ConnectionLiveData(requireActivity()).observe(this, Observer {
            changeConnectionStatus(it)
        })
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
     * Return the actual network connection status
     */
    open fun changeConnectionStatus(connectionOk : Boolean){}

    open fun init(){}

    /**
     * Layout resource to be added in Fragment
     */
    abstract fun layout(): Int
}