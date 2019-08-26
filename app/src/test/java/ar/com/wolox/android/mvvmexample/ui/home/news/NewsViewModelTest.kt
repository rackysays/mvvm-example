package ar.com.wolox.android.mvvmexample.ui.home.news

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import ar.com.wolox.android.mvvmexample.model.New
import ar.com.wolox.android.mvvmexample.model.Resource
import ar.com.wolox.android.mvvmexample.network.NewService
import ar.com.wolox.android.mvvmexample.network.utils.ApiResponse
import ar.com.wolox.android.mvvmexample.util.UserSession
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.MockitoAnnotations
import retrofit2.Response

@RunWith(JUnit4::class)
class NewsViewModelTest {

    @Mock lateinit var userSession : UserSession
    @Mock lateinit var newService: NewService
    @Mock lateinit var listNewsObserver: Observer<Resource<List<New>>>

    private var expectedServiceResponse = MutableLiveData<ApiResponse<List<New>>>()
    private lateinit var viewModel: NewsViewModel

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(newService.getNewsByLimit(0,10)).
            thenReturn(expectedServiceResponse)
        viewModel = NewsViewModel(userSession, newService)
        viewModel.observeNewsList().observeForever(listNewsObserver)

    }

    @After
    fun finish() {
        viewModel.observeNewsList().removeObserver(listNewsObserver)
        expectedServiceResponse = MutableLiveData()
    }

    //This one has a different requirement from others, that's why is instance one more time
    @Test
    fun userIdAlreadyStoredIsReturned(){
        Mockito.`when`(userSession.userId).thenReturn(MOCKED_USER_ID)
        viewModel = NewsViewModel(userSession, newService)
        assertEquals(MOCKED_USER_ID, viewModel.observeUserStored().value)
    }

    @Test
    fun listNewsIsFirstCalled(){
        expectedServiceResponse.postValue(ApiResponse(Response.success(NEW_LIST)))
        assertEquals(NEW_SUCCESS, viewModel.observeNewsList().value)
    }

    @Test
    fun listNewsPaginationIsCalled(){
        Mockito.`when`(newService.getNewsByLimit(10,10)).
            thenReturn(expectedServiceResponse)
        expectedServiceResponse.postValue(ApiResponse(Response.success(NEW_LIST)))
        viewModel.nextPage(10)
        assertEquals(NEW_SUCCESS_PAGINATION, viewModel.observeNewsList().value)
    }

    //Service should be called twice, first by init, and second time by refresh
    @Test
    fun offsetIsResetOnRefreshData(){
        viewModel.refreshData()
        Mockito.verify(newService, times(2)).getNewsByLimit(0,10)
    }

    companion object {
        private const val MOCKED_USER_ID = "1"
        private val NEW_MOCKED = New(1,1,"2016-07-18T14:00:29.985Z",
            "¿Famosos y sólo amigos?", "http://bucket1.glanacion.com/anexos/fotos/70/dia-del-amigo-2236070w620.jpg",
            "Ser súper estrellas e íntimos amigos tiene sus desventajas, al menos para George. " +
                    "Su esposa, Amal, es muy celosa de Julia e irrumpió varias veces en las grabaciones " +
                    "de su última peli juntos, aunque nunca pescó nada raro.", listOf(1,3))
        private val NEW_LIST = listOf(NEW_MOCKED)
        private val NEW_LIST_PAGINATION = listOf(NEW_MOCKED, NEW_MOCKED)
        private val NEW_SUCCESS = Resource.success(NEW_LIST,false)
        private val NEW_SUCCESS_PAGINATION = Resource.success(NEW_LIST_PAGINATION,false)
    }
}