package ar.com.wolox.android.mvvmexample.ui.newdetail

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ar.com.wolox.android.mvvmexample.model.New
import ar.com.wolox.android.mvvmexample.model.Resource
import ar.com.wolox.android.mvvmexample.network.NewService
import ar.com.wolox.android.mvvmexample.network.utils.ApiResponse
import ar.com.wolox.android.mvvmexample.util.AbsentLiveData
import ar.com.wolox.android.mvvmexample.util.Extras.News.NEW
import ar.com.wolox.android.mvvmexample.util.NetworkSimpleBoundResource
import ar.com.wolox.android.mvvmexample.util.UserSession
import javax.inject.Inject

class NewDetailViewModel @Inject constructor(userSession: UserSession,
                                             private val newService: NewService): ViewModel() {

    private val new : MutableLiveData<New> = MutableLiveData()
    private val userIdStored = MutableLiveData<String>()
    private val newLiveData: LiveData<Resource<New>>
    private val likeLiveData: LiveData<Resource<New>>
    private val refreshed = MutableLiveData<Int>()
    private val switchLike = MutableLiveData<Boolean>()

    init {
        userIdStored.postValue(userSession.userId)
        newLiveData = Transformations.switchMap(refreshed){
            it?.let {
                getRefreshedData(it)
            }?: AbsentLiveData.create()
        }
        likeLiveData = Transformations.switchMap(switchLike){
            it?.let {
                switchLikeService()
            }?: AbsentLiveData.create()
        }
    }

    fun loadArguments(arguments: Bundle?) {
        arguments?.let { this.new.postValue(it.getSerializable(NEW) as New) }
    }

    fun observeNew() : LiveData<New> = new

    fun observeUserStored() : LiveData<String> = userIdStored

    fun observeRefreshStatus() = newLiveData

    fun observeLikeStatus() = likeLiveData

    fun onRefreshNewDetail() { new.value?.id.let { refreshed.postValue(it) } }

    fun onLikeClicked() { switchLike() }

    fun getImageUrl() : String = new.value?.picture!!

    private fun getRefreshedData(id: Int): LiveData<Resource<New>> {
        return object : NetworkSimpleBoundResource<New, New>(){

            override fun transformResult(item: New): LiveData<New> =
                MutableLiveData<New>().apply {
                    value = item
                    new.postValue(item)
                }

            override fun createCall(): LiveData<ApiResponse<New>> =
                newService.getNewsById(id)

        }.asLiveData()
    }

    private fun switchLikeService(): LiveData<Resource<New>> {
        return object : NetworkSimpleBoundResource<New, New>(){

            override fun transformResult(item: New): LiveData<New> =
                MutableLiveData<New>().apply {
                    value = item
                    new.postValue(item)
                }

            override fun createCall(): LiveData<ApiResponse<New>> =
                newService.setNewLike(new.value?.id!!, new.value!!)
        }.asLiveData()
    }

    private fun switchLike(){
        userIdStored.value?.toInt()?.let { id ->
            new.value?.let { newValue->
                val likesListed = mutableListOf<Int>().apply { addAll(newValue.likes) }
                (likesListed).apply {
                    when (id in likesListed) {
                        true -> remove(id)
                        false -> add(id)
                    }
                }
                new.postValue(newValue.copy(likes = likesListed as List<Int>))
                switchLike.postValue(true)
            }
        }
    }
}