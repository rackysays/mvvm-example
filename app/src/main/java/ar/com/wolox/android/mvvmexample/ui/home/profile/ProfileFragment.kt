package ar.com.wolox.android.mvvmexample.ui.home.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import ar.com.wolox.android.mvvmexample.R
import ar.com.wolox.android.mvvmexample.databinding.FragmentProfileBinding
import ar.com.wolox.android.mvvmexample.ui.base.BaseFragment

class ProfileFragment : BaseFragment<FragmentProfileBinding>(){

    override fun layout(): Int = R.layout.fragment_profile

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layout(),container,false)
        return binding.root
    }
}