package ar.com.wolox.android.mvvmexample.ui.newdetail

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import ar.com.wolox.android.mvvmexample.model.New
import ar.com.wolox.android.mvvmexample.model.Resource
import ar.com.wolox.android.mvvmexample.network.NewService
import ar.com.wolox.android.mvvmexample.network.utils.ApiResponse
import ar.com.wolox.android.mvvmexample.util.Extras.News.NEW
import ar.com.wolox.android.mvvmexample.util.UserSession
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

@RunWith(JUnit4::class)
class NewDetailViewModelTest {

    @Mock lateinit var userSession : UserSession
    @Mock lateinit var newService: NewService
    @Mock lateinit var bundle: Bundle
    @Mock lateinit var newObserver: Observer<New>
    @Mock lateinit var newRefreshObserver: Observer<New>
    @Mock lateinit var newLikeObserver: Observer<New>
    private var expectedServiceResponse = MutableLiveData<ApiResponse<New>>()

    private lateinit var viewModel: NewDetailViewModel

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)

        Mockito.`when`(userSession.userId).thenReturn("1")

        viewModel = NewDetailViewModel(userSession, newService)

        //We put this here, to have always available the new pass to the fragment
        Mockito.`when`(bundle.getSerializable(NEW)).thenReturn(NEW_MOCKED)
        viewModel.observeNew().observeForever { newObserver }
        viewModel.loadArguments(bundle)
    }

    @After
    fun finish(){
        viewModel.observeNew().removeObserver { newObserver }
    }

    @Test
    fun newIsShownAfterArgumentReceived() {
        Assert.assertEquals(NEW_MOCKED,viewModel.observeNew().value)
    }

    @Test
    fun newIsRefreshedByUser(){
        viewModel.observeRefreshStatus().observeForever { newRefreshObserver }

        Mockito.`when`(newService.getNewsById(1)).thenReturn(expectedServiceResponse)
        expectedServiceResponse.postValue(ApiResponse(Response.success(NEW_MOCKED)))
        viewModel.onRefreshNewDetail()
        Assert.assertEquals(NEW_SUCCESS,viewModel.observeRefreshStatus().value)
        viewModel.observeRefreshStatus().removeObserver { newRefreshObserver }
    }

    @Test
    fun newIsLikedByUser(){
        viewModel.observeLikeStatus().observeForever { newLikeObserver }

        //At first we mock de first new value, like the user never liked the new
        Mockito.`when`(bundle.getSerializable(NEW)).thenReturn(NEW_MOCKED_ALREADY_DISLIKE)
        viewModel.loadArguments(bundle)

        Mockito.`when`(newService.setNewLike(1, NEW_MOCKED_ALREADY_LIKE)).thenReturn(expectedServiceResponse)
        expectedServiceResponse.postValue(ApiResponse(Response.success(NEW_MOCKED_ALREADY_LIKE)))

        viewModel.onLikeClicked()
        Assert.assertEquals(NEW_MOCKED_ALREADY_LIKE,viewModel.observeLikeStatus().value?.data)
    }

    @Test
    fun newIsDislikedByUser(){
        viewModel.observeLikeStatus().observeForever { newLikeObserver }

        Mockito.`when`(newService.setNewLike(1, NEW_MOCKED_ALREADY_DISLIKE)).thenReturn(expectedServiceResponse)
        expectedServiceResponse.postValue(ApiResponse(Response.success(NEW_MOCKED_ALREADY_DISLIKE)))

        viewModel.onLikeClicked()
        Assert.assertEquals(NEW_MOCKED_ALREADY_DISLIKE,viewModel.observeLikeStatus().value?.data)
    }


    companion object {
        private val NEW_MOCKED = New(1,1,"2016-07-18T14:00:29.985Z",
            "¿Famosos y sólo amigos?", "http://bucket1.glanacion.com/anexos/fotos/70/dia-del-amigo-2236070w620.jpg",
            "Ser súper estrellas e íntimos amigos tiene sus desventajas, al menos para George. " +
                    "Su esposa, Amal, es muy celosa de Julia e irrumpió varias veces en las grabaciones " +
                    "de su última peli juntos, aunque nunca pescó nada raro.", listOf(1,3))

        private val NEW_MOCKED_ALREADY_LIKE = New(1,1,"2016-07-18T14:00:29.985Z",
            "¿Famosos y sólo amigos?", "http://bucket1.glanacion.com/anexos/fotos/70/dia-del-amigo-2236070w620.jpg",
            "Ser súper estrellas e íntimos amigos tiene sus desventajas, al menos para George. " +
                    "Su esposa, Amal, es muy celosa de Julia e irrumpió varias veces en las grabaciones " +
                    "de su última peli juntos, aunque nunca pescó nada raro.", listOf(3,1))

        private val NEW_MOCKED_ALREADY_DISLIKE = New(1,1,"2016-07-18T14:00:29.985Z",
            "¿Famosos y sólo amigos?", "http://bucket1.glanacion.com/anexos/fotos/70/dia-del-amigo-2236070w620.jpg",
            "Ser súper estrellas e íntimos amigos tiene sus desventajas, al menos para George. " +
                    "Su esposa, Amal, es muy celosa de Julia e irrumpió varias veces en las grabaciones " +
                    "de su última peli juntos, aunque nunca pescó nada raro.", listOf(3))
        private val NEW_SUCCESS = Resource.success(NEW_MOCKED,false)
    }
}