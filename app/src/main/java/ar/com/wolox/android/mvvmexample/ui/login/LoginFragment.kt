package ar.com.wolox.android.mvvmexample.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import ar.com.wolox.android.mvvmexample.R
import ar.com.wolox.android.mvvmexample.databinding.LoginFragmentBinding
import ar.com.wolox.android.mvvmexample.ui.base.BaseFragment
import timber.log.Timber

class LoginFragment: BaseFragment() {

    private lateinit var binding : LoginFragmentBinding

    override fun layout(): Int = R.layout.fragment_login

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layout(),container,false)
        return binding.root
    }

    override fun init() {
        Timber.d("init(): Login fragment was initialized")
    }

    override fun setListener() {
        super.setListener()
        binding.vLoginButton.setOnClickListener {
            Timber.d("logIn was clicked")
        }
        binding.vSignupButton.setOnClickListener {
            Timber.d("signUp was clicked")
        }
    }
}