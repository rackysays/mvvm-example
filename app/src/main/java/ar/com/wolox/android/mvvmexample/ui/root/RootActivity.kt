package ar.com.wolox.android.mvvmexample.ui.root

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import ar.com.wolox.android.mvvmexample.R
import ar.com.wolox.android.mvvmexample.model.Status
import ar.com.wolox.android.mvvmexample.ui.base.BaseActivity
import ar.com.wolox.android.mvvmexample.ui.home.HomePageActivity
import ar.com.wolox.android.mvvmexample.ui.login.LoginActivity
import ar.com.wolox.android.mvvmexample.util.observeLiveData
import javax.inject.Inject

class RootActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: RootViewModel

    override fun init() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RootViewModel::class.java)
        observeLiveData()
    }

    private fun observeLiveData(){
        observeLiveData(viewModel.observeLiveDataLoginValidation()){
            when(it.status) {
                Status.SUCCESS -> goToHomeScreenPage()
                Status.ERROR -> goToLoginScreen()
                Status.LOADING -> Unit
            }
        }
    }

    private fun goToLoginScreen() {
        startActivity(Intent(this, LoginActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }

    private fun goToHomeScreenPage() {
        startActivity(Intent(this, HomePageActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }

    override fun layout(): Int = R.layout.activity_base

    override fun changeConnectionStatus(connectionOk: Boolean) {
        super.changeConnectionStatus(connectionOk)
        if (!connectionOk) {
            Toast.makeText(this, ERROR_NETWORK, Toast.LENGTH_LONG).show()
            goToLoginScreen()
        }
    }

    companion object {
        const val ERROR_NETWORK = R.string.network_error_message
    }
}