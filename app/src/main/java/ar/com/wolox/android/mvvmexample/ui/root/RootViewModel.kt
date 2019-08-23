package ar.com.wolox.android.mvvmexample.ui.root

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import ar.com.wolox.android.mvvmexample.model.Resource
import ar.com.wolox.android.mvvmexample.model.User
import ar.com.wolox.android.mvvmexample.model.UserCredentials
import ar.com.wolox.android.mvvmexample.network.LoginService
import ar.com.wolox.android.mvvmexample.network.utils.ApiResponse
import ar.com.wolox.android.mvvmexample.ui.base.BaseViewModel
import ar.com.wolox.android.mvvmexample.util.AbsentLiveData
import ar.com.wolox.android.mvvmexample.util.NetworkSimpleBoundResource
import ar.com.wolox.android.mvvmexample.util.UserSession
import javax.inject.Inject

class RootViewModel @Inject constructor(private val userSession: UserSession,
                                        private val loginService: LoginService) : BaseViewModel() {

    // Live Data for validation of Network Login
    private val loginLiveData : LiveData<Resource<User>>
    // User credentials to log in
    private val userCredentials : MutableLiveData<UserCredentials> = MutableLiveData()

    init {
        loginLiveData  = Transformations.switchMap(userCredentials) {
            userCredentials.value?.let {
                if (it.username.isEmpty() && it.password.isEmpty()){
                    liveDataUserLoginEmpty()
                } else {
                    liveDataUserLogin(it.username, it.password)
                }

            } ?: AbsentLiveData.create()
        }
        firstValidation()
    }

    fun observeLiveDataLoginValidation() = loginLiveData

    private fun firstValidation() {
        if (userSession.username != null && userSession.password != null) {
            userCredentials.postValue(
                UserCredentials(
                    userSession.username!!,
                    userSession.password!!
                )
            )
        } else {
            userCredentials.postValue(UserCredentials("",""))
        }
    }

    // This function is called when a new username and password is set
    private fun liveDataUserLogin(username: String, password: String) : LiveData<Resource<User>> {
        return object : NetworkSimpleBoundResource<User, List<User>>(){

            override fun transformResult(item: List<User>): LiveData<User> = MutableLiveData<User>().
                apply {
                    if (item.isNotEmpty()) {
                        value = item[0]
                    }
                }

            override fun createCall(): LiveData<ApiResponse<List<User>>> = loginService.getUserByLiveCredentials(username,password)

        }.asLiveData()
    }

    private fun liveDataUserLoginEmpty() : LiveData<Resource<User>> =
        MutableLiveData<Resource<User>>().apply {
            postValue(Resource.error(NetworkSimpleBoundResource.NULL_DATA, null))}
}

