package ar.com.wolox.android.mvvmexample.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ar.com.wolox.android.mvvmexample.util.ConnectionLiveData
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment<T : ViewDataBinding> : DaggerFragment(){

    protected lateinit var binding : T

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layout(),container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
     * Layout resource to be added in Fragment
     */
    abstract fun layout(): Int

    /**
     * Return the actual network connection status
     */
    open fun changeConnectionStatus(connectionOk : Boolean){}
}