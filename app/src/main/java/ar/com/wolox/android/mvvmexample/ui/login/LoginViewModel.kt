package ar.com.wolox.android.mvvmexample.ui.login

import android.content.Context
import androidx.core.util.PatternsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ar.com.wolox.android.mvvmexample.R
import ar.com.wolox.android.mvvmexample.ui.base.BaseViewModel
import ar.com.wolox.android.mvvmexample.util.Extras.UserLogin.LOGIN_SUCCESS
import ar.com.wolox.android.mvvmexample.util.Extras.UserLogin.PASSWORD
import ar.com.wolox.android.mvvmexample.util.Extras.UserLogin.USERNAME
import ar.com.wolox.android.mvvmexample.util.UserSession
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val userSession: UserSession, private val context: Context): BaseViewModel(){

    private var userStored : MutableLiveData<String> = MutableLiveData()
    private var validation : MutableLiveData<Pair<String, String>> = MutableLiveData()

    init {
        userStored.postValue(userSession.username)
    }

    fun observeUserStored() : LiveData<String> = userStored

    fun observeValidationLogin() : LiveData<Pair<String, String>> = validation

    fun onLoginClicked(username: String, password: String){
        if (username.isEmpty() && password.isEmpty()){
            validation.postValue(Pair(USERNAME,context.resources.getString(EMPTY_FIELD)))
        }else if(username.isEmpty()) {
            validation.postValue(Pair(USERNAME, context.resources.getString(EMPTY_FIELD)))
        }else if (password.isEmpty()){
            validation.postValue(Pair(PASSWORD, context.resources.getString(EMPTY_FIELD)))
        } else if(evaluateUsernameFormat(username)){
            validation.postValue(Pair(LOGIN_SUCCESS, EMPTY))
            userStored.postValue(username)
            userSession.apply {
                this.username = username
                this.password = password
            }
        } else {
            validation.postValue(Pair(USERNAME, context.resources.getString(ERROR_USERNAME)))
        }
    }

    private fun evaluateUsernameFormat(email: String) : Boolean {
        return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }

    companion object {
        const val EMPTY = ""
        const val EMPTY_FIELD = R.string.login_required_field
        const val ERROR_USERNAME = R.string.login_wrong_username_format
    }

}