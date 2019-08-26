package ar.com.wolox.android.mvvmexample.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import ar.com.wolox.android.mvvmexample.R
import ar.com.wolox.android.mvvmexample.databinding.FragmentHomePageBinding
import ar.com.wolox.android.mvvmexample.ui.base.BaseFragment

class HomePageFragment: BaseFragment(){

    private lateinit var binding: FragmentHomePageBinding

    override fun layout(): Int = R.layout.fragment_homepage

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layout(),container,false)
        requireActivity().apply {
            binding.adapter = TabSelectionAdapter(this, this.supportFragmentManager)
        }
        return binding.root
    }
}