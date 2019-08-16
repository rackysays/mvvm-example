package ar.com.wolox.android.mvvmexample.ui.login

import ar.com.wolox.android.mvvmexample.ui.base.BaseViewModel
import ar.com.wolox.android.mvvmexample.util.UserSession
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val userSession: UserSession): BaseViewModel(){

    fun onLoginClicked(username: String, password: String){
        userSession.username = username
        userSession.password = password
    }
}