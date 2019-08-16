package ar.com.wolox.android.mvvmexample.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import ar.com.wolox.android.mvvmexample.R
import ar.com.wolox.android.mvvmexample.databinding.LoginFragmentBinding
import ar.com.wolox.android.mvvmexample.ui.base.BaseFragment
import ar.com.wolox.android.mvvmexample.util.Extras.UserLogin.LOGIN_FAIL
import ar.com.wolox.android.mvvmexample.util.Extras.UserLogin.LOGIN_SUCCESS
import ar.com.wolox.android.mvvmexample.util.Extras.UserLogin.PASSWORD
import ar.com.wolox.android.mvvmexample.util.Extras.UserLogin.USERNAME
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
        binding.viewModel = viewModel
        observeLiveData(viewModel.observeUserStored()){
            binding.usernameStored = it
        }
        observeLiveData(viewModel.observeValidationLogin()){
            when(it.first){
                USERNAME -> {
                    binding.vLoginUsername.error = it.second
                    binding.vLoginPassword.error = null
                }
                PASSWORD -> {
                    binding.vLoginPassword.error = it.second
                    binding.vLoginUsername.error = null
                }
                LOGIN_SUCCESS -> {
                    binding.vLoginUsername.error = null
                    binding.vLoginPassword.error = null
                }
                LOGIN_FAIL -> {
                    Toast.makeText(requireContext(),it.second,Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun setListener() {
        binding.vSignupButton.setOnClickListener {
            Timber.d("signUp was clicked")
        }
    }
}