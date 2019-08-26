package ar.com.wolox.android.mvvmexample.ui.home.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import ar.com.wolox.android.mvvmexample.model.New
import ar.com.wolox.android.mvvmexample.model.Resource
import ar.com.wolox.android.mvvmexample.network.NewService
import ar.com.wolox.android.mvvmexample.network.utils.ApiResponse
import ar.com.wolox.android.mvvmexample.ui.base.BaseViewModel
import ar.com.wolox.android.mvvmexample.ui.home.news.NewsFragment.Companion.LIMIT
import ar.com.wolox.android.mvvmexample.util.AbsentLiveData
import ar.com.wolox.android.mvvmexample.util.NetworkSimpleBoundResource
import ar.com.wolox.android.mvvmexample.util.UserSession
import javax.inject.Inject

class NewsViewModel @Inject constructor(userSession: UserSession,
                                        private val newService: NewService) : BaseViewModel() {

    private val userIdStored = MutableLiveData<String>()
    private var offset = MutableLiveData<Int>()
    private val newsList: LiveData<Resource<List<New>>>
    private val newsListStored  = mutableListOf<New>()

    init {
        userIdStored.postValue(userSession.userId)
        newsList = Transformations.switchMap(offset){
            it?.let {
                getNewsLiveData(it)
            }?: AbsentLiveData.create()
        }
        offset.postValue(0)
    }

    fun observeUserStored() : LiveData<String> = userIdStored

    fun observeNewsList() : LiveData<Resource<List<New>>> = newsList

    fun observeNewsListStatusValue() = observeNewsList().value

    fun refreshData() {
        newsListStored.clear()
        offset.postValue(0)
    }

    fun nextPage(offset: Int) { this.offset.postValue(offset) }

    private fun getNewsLiveData(offset: Int) : LiveData<Resource<List<New>>> {
        return object : NetworkSimpleBoundResource<List<New>, List<New>>(){

            override fun transformResult(item: List<New>): LiveData<List<New>> =
                MutableLiveData<List<New>>().apply {
                    newsListStored.addAll(item)
                    value = newsListStored
                }

            override fun createCall(): LiveData<ApiResponse<List<New>>> =
                newService.getNewsByLimit(offset, LIMIT)

        }.asLiveData()
    }
}