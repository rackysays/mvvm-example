package ar.com.wolox.android.mvvmexample.ui.login

import android.content.Intent
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
import ar.com.wolox.android.mvvmexample.model.Status
import ar.com.wolox.android.mvvmexample.ui.base.BaseFragment
import ar.com.wolox.android.mvvmexample.ui.home.HomePageActivity
import ar.com.wolox.android.mvvmexample.util.NetworkSimpleBoundResource.NetworkSimpleBoundErrors.NULL_DATA
import ar.com.wolox.android.mvvmexample.util.NetworkSimpleBoundResource.NetworkSimpleBoundErrors.UNKNOWN
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

    override fun observeLiveData(){
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)

        observeLiveData(viewModel.observeUserStored()){
            binding.usernameStored = it
        }

        observeLiveData(viewModel.observeValidationLogin()){
            binding.validationField = it
        }

        observeLiveData(viewModel.observeLiveDataUserValidation()){
            when(it.status){
                Status.LOADING -> binding.isLoading = true
                Status.SUCCESS -> {
                    binding.isLoading = false
                    goToHomeScreenPage()
                }
                Status.ERROR -> {
                    binding.isLoading = false
                    val error = when(it.message) {
                        NULL_DATA -> ERROR_LOGIN
                        UNKNOWN -> ERROR_UNKNOWN
                        else -> ERROR_UNKNOWN
                    }
                    Toast.makeText(requireActivity(),error,Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun setListener() {
        binding.vLoginButton.setOnClickListener {
            binding.apply {
                viewModel.onLoginClicked(vLoginUsername.text.toString(), vLoginPassword.text.toString())
            }
        }
        binding.vSignupButton.setOnClickListener {
            Timber.d("signUp was clicked")
        }
    }


    override fun changeConnectionStatus(connectionOk: Boolean) {
        super.changeConnectionStatus(connectionOk)
        if (!connectionOk) {
            Toast.makeText(requireActivity(), ERROR_NETWORK, Toast.LENGTH_LONG).show()
        }
        binding.vLoginButton.isEnabled = connectionOk
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveFormBeforeDestroy(binding.vLoginUsername.text.toString())
    }

    private fun goToHomeScreenPage(){
        startActivity(
            Intent(requireActivity(), HomePageActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        requireActivity().finish()
    }

    companion object {
        const val ERROR_LOGIN = R.string.login_error_username_password
        const val ERROR_UNKNOWN = R.string.network_error_unknows
        const val ERROR_NETWORK = R.string.network_error_message
    }
}