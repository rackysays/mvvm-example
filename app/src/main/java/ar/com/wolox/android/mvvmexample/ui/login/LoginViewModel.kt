package ar.com.wolox.android.mvvmexample.ui.login

import android.content.Context
import androidx.annotation.NonNull
import androidx.core.util.PatternsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ar.com.wolox.android.mvvmexample.R
import ar.com.wolox.android.mvvmexample.model.User
import ar.com.wolox.android.mvvmexample.model.ValidationMessage
import ar.com.wolox.android.mvvmexample.network.LoginService
import ar.com.wolox.android.mvvmexample.ui.base.BaseViewModel
import ar.com.wolox.android.mvvmexample.util.Extras.UserLogin.LOGIN_FAIL
import ar.com.wolox.android.mvvmexample.util.Extras.UserLogin.LOGIN_SUCCESS
import ar.com.wolox.android.mvvmexample.util.Extras.UserLogin.PASSWORD
import ar.com.wolox.android.mvvmexample.util.Extras.UserLogin.USERNAME
import ar.com.wolox.android.mvvmexample.util.UserSession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val userSession: UserSession,
                                         private val context: Context,
                                         private val loginService: LoginService): BaseViewModel(){

    private var userStored : MutableLiveData<String> = MutableLiveData()
    private var validation : MutableLiveData<ValidationMessage> = MutableLiveData()

    init {
        userStored.postValue(userSession.username)
    }

    fun observeUserStored() : LiveData<String> = userStored

    fun observeValidationLogin() : LiveData<ValidationMessage> = validation

    fun onLoginClicked(username: String, password: String){
        if (username.isEmpty() && password.isEmpty()){
            validation.postValue(ValidationMessage(USERNAME,context.resources.getString(EMPTY_FIELD)))
        }else if(username.isEmpty()) {
            validation.postValue(ValidationMessage(USERNAME, context.resources.getString(EMPTY_FIELD)))
        }else if (password.isEmpty()){
            validation.postValue(ValidationMessage(PASSWORD, context.resources.getString(EMPTY_FIELD)))
        } else if(evaluateUsernameFormat(username)){
            userStored.postValue(username)
            loginUser(username, password)
        } else {
            validation.postValue(ValidationMessage(USERNAME, context.resources.getString(ERROR_USERNAME)))
        }
    }

    private fun evaluateUsernameFormat(email: String) : Boolean {
        return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun loginUser(@NonNull username: String, @NonNull password: String){
        loginService.getUserByCredentials(username,password).enqueue(object: Callback<List<User>>{
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.body()?.size!! > 0){
                    userSession.apply {
                        this.username = username
                        this.password = password
                    }
                    validation.postValue(ValidationMessage(LOGIN_SUCCESS, EMPTY))
                } else {
                    validation.postValue(ValidationMessage(LOGIN_FAIL, context.resources.getString(ERROR_LOGIN)))
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                validation.postValue(ValidationMessage(LOGIN_FAIL, context.resources.getString(ERROR_UNKNOWN)))
            }
        })
    }

    companion object {
        const val EMPTY = ""
        const val EMPTY_FIELD = R.string.login_required_field
        const val ERROR_USERNAME = R.string.login_wrong_username_format
        const val ERROR_LOGIN = R.string.login_error_username_password
        const val ERROR_UNKNOWN = R.string.network_error_unknows
    }

}