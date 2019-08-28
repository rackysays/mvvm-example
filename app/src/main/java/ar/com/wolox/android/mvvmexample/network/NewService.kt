package ar.com.wolox.android.mvvmexample.network

import androidx.lifecycle.LiveData
import ar.com.wolox.android.mvvmexample.model.New
import ar.com.wolox.android.mvvmexample.network.utils.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface NewService {

    @GET("/news")
    fun getNewsByLimit(@Query("offset") offset: Int, @Query("limit") limit: Int): LiveData<ApiResponse<List<New>>>

    @GET("/news/{newID}")
    fun getNewsById(@Path("newID") newId: Int): LiveData<ApiResponse<New>>

    @PUT("/news/{newID}")
    fun setNewLike(@Path("newID") newId: Int, @Body new: New): LiveData<ApiResponse<New>>
}