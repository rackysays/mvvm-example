package ar.com.wolox.android.mvvmexample.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import ar.com.wolox.android.mvvmexample.R
import ar.com.wolox.android.mvvmexample.databinding.LoginFragmentBinding
import ar.com.wolox.android.mvvmexample.ui.base.BaseFragment
import ar.com.wolox.android.mvvmexample.util.observeLiveData
import timber.log.Timber
import javax.inject.Inject

class LoginFragment: BaseFragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding : LoginFragmentBinding
    private lateinit var viewModel: LoginViewModel

    override fun layout(): Int = R.layout.fragment_login

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layout(),container,false)
        return binding.root
    }

    override fun init() { Timber.d("init(): Login fragment was initialized") }

    override fun observeLiveData(){
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)
    }

    override fun setListener() {
        binding.vLoginButton.setOnClickListener {
            viewModel.onLoginClicked(binding.vLoginUsername.text.toString(), binding.vLoginPassword.text.toString())
        }
        binding.vSignupButton.setOnClickListener {
            Timber.d("signUp was clicked")
        }
    }
}