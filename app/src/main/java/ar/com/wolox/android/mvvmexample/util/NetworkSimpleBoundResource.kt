package ar.com.wolox.android.mvvmexample.util

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import ar.com.wolox.android.mvvmexample.model.Resource
import ar.com.wolox.android.mvvmexample.network.utils.ApiResponse

/**
 *
 * ResultType: Type for the Resource data.
 * RequestType: Type for the API response.
 */
abstract class NetworkSimpleBoundResource<ResultType, RequestType> {

    private val result: MediatorLiveData<Resource<ResultType>> = MediatorLiveData()

    init {
        fetchFromNetwork()
    }

    /**
     * Method to get data from network call
     */
    private fun fetchFromNetwork() {
        val apiResponse = createCall()
        result.postValue(Resource.loading(null))
        result.addSource(apiResponse) { response ->
            response?.let {
                when (response.isSuccessful) {
                    true -> {
                        response.body?.let {
                            val loaded = transformResult(it)
                            loaded.value?.let {
                                result.addSource(loaded){ newData ->
                                    newData?.let {
                                        setValue(Resource.success(newData,false))
                                    }
                                }
                            }?:run {
                                result.postValue(Resource.error(NULL_DATA,null))
                            }
                        }
                    }
                    false -> {
                        onFetchFailed()
                        response.message?.let {
                            result.postValue(Resource.error(it,null))
                        }
                    }
                }
            }?: run{
                result.postValue(Resource.error(UNKNOWN,null))
            }
        }
    }

    // Called to transform result from call to needed result.
    // It was created, because not all the data receive is what is needed for the Activity/Fragment
    @MainThread
    protected abstract fun transformResult(item: RequestType): LiveData<ResultType>

    // Called to create the API call.
    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>

    // Called when the fetch fails. The child class may want to reset components
    // like rate limiter.
    protected open fun onFetchFailed() {}

    // Returns a LiveData object that represents the resource that's implemented
    // in the base class.
    fun asLiveData(): LiveData<Resource<ResultType>> = result

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        result.value = newValue
    }

    // Possible errors
    companion object NetworkSimpleBoundErrors {
        const val UNKNOWN = "Unknown error"
        const val NULL_DATA = "Null transformation"
    }

}
