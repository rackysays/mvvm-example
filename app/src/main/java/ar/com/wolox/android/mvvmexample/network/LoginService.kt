package ar.com.wolox.android.mvvmexample.network

import androidx.lifecycle.LiveData
import ar.com.wolox.android.mvvmexample.model.User
import ar.com.wolox.android.mvvmexample.network.utils.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface LoginService {

    @GET("/users")
    fun getUserByLiveCredentials(@Query("email") username: String,
                                 @Query("password") password: String): LiveData<ApiResponse<List<User>>>
}