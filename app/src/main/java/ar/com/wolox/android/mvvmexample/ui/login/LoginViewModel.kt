package ar.com.wolox.android.mvvmexample.ui.login

import androidx.core.util.PatternsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ar.com.wolox.android.mvvmexample.network.LoginService
import ar.com.wolox.android.mvvmexample.network.utils.ApiResponse
import ar.com.wolox.android.mvvmexample.ui.base.BaseViewModel
import ar.com.wolox.android.mvvmexample.util.AbsentLiveData
import ar.com.wolox.android.mvvmexample.util.NetworkSimpleBoundResource
import ar.com.wolox.android.mvvmexample.util.UserSession
import androidx.lifecycle.Transformations.switchMap
import ar.com.wolox.android.mvvmexample.model.*
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val userSession: UserSession,
                                         private val loginService: LoginService): BaseViewModel(){

    // User stored from SharedPreferences
    private val userStored : MutableLiveData<String> = MutableLiveData()
    // User credentials to log in
    private val userCredentials : MutableLiveData<UserCredentials> = MutableLiveData()
    // Live Data for validation of Login Form
    private val loginValidation : MutableLiveData<ValidationMessage> = MutableLiveData()
    // Live Data for validation of Network Login
    private val loginLiveData :  LiveData<Resource<User>>

    // All LiveData<?> must be initialized here
    init {
        userStored.postValue(userSession.username)
        loginLiveData = switchMap(userCredentials){
            userCredentials.value?.let {
                liveDataUserLogin(it.username, it.password)
            }?: AbsentLiveData.create()
        }
    }

    // Observe of user from SharedPreference
    fun observeUserStored() : LiveData<String> = userStored

    // Observe form validation
    fun observeValidationLogin() : LiveData<ValidationMessage> = loginValidation

    // Observe network call for Log In
    fun observeLiveDataUserValidation() :  LiveData<Resource<User>> = loginLiveData

    // Called when logIn button is clicked
    fun onLoginClicked(username: String, password: String){
        if (username.isEmpty() && password.isEmpty()){
            loginValidation.postValue(ValidationMessage(FormField.USERNAME,ErrorMessage.EMPTY_FIELD))
        }else if(username.isEmpty()) {
            loginValidation.postValue(ValidationMessage(FormField.USERNAME,ErrorMessage.EMPTY_FIELD))
        }else if (password.isEmpty()){
            loginValidation.postValue(ValidationMessage(FormField.PASSWORD, ErrorMessage.EMPTY_FIELD))
        } else if(evaluateUsernameFormat(username)){
            userCredentials.postValue(UserCredentials(username,password))
        } else {
            loginValidation.postValue(ValidationMessage(FormField.USERNAME, ErrorMessage.WRONG_FORMAT))
        }
    }

    // Save username from the field in SharedPreferences before is destroyed
    fun saveFormBeforeDestroy(username: String){
        userSession.username = username
    }

    // Validation of email address format
    private fun evaluateUsernameFormat(email: String) : Boolean {
        return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }

    // This function is called when a new username and password is set
    private fun liveDataUserLogin(username: String, password: String) : LiveData<Resource<User>> {
        return object : NetworkSimpleBoundResource<User, List<User>>(){

            override fun transformResult(item: List<User>): LiveData<User> = MutableLiveData<User>().
                apply {
                    if (item.isNotEmpty()) {
                        item[0].let {
                            userSession.username = it.email
                            userSession.password = it.password
                            value = it
                        }
                    }
                }

            override fun createCall(): LiveData<ApiResponse<List<User>>> =
                loginService.getUserByLiveCredentials(username,password)

        }.asLiveData()
    }
}