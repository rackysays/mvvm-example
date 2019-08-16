package ar.com.wolox.android.mvvmexample.network

import ar.com.wolox.android.mvvmexample.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LoginService {

    @GET("/users")
    fun getUserByCredentials(@Query("email") username: String, @Query("password") password: String): Call<List<User>>
}