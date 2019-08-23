package ar.com.wolox.android.mvvmexample.ui.root

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import ar.com.wolox.android.mvvmexample.model.*
import ar.com.wolox.android.mvvmexample.network.LoginService
import ar.com.wolox.android.mvvmexample.network.utils.ApiResponse
import ar.com.wolox.android.mvvmexample.util.NetworkSimpleBoundResource
import ar.com.wolox.android.mvvmexample.util.UserSession
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response

@RunWith(JUnit4::class)
class RootViewModelTest {

    @Mock
    lateinit var loginService : LoginService
    @Mock
    lateinit var userSession : UserSession
    @Mock
    lateinit var loginValidationObserver: Observer<Resource<User>>

    private lateinit var viewModel: RootViewModel

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
        viewModel = RootViewModel(userSession, loginService)
    }

    @Test
    fun userWasNotPreviouslyLogged() {
        viewModel.observeLiveDataLoginValidation().observeForever(loginValidationObserver)
        assertEquals(NO_USER_DETECTED, viewModel.observeLiveDataLoginValidation().value)
    }

    @Test
    fun callingLoginServiceSuccess(){
        Mockito.`when`(userSession.username).thenReturn("usermocked@example.com")
        Mockito.`when`(userSession.password).thenReturn("12345678")
        `when`(loginService.getUserByLiveCredentials("usermocked@example.com","12345678")).
            thenReturn(EXPECTED_SERVICE_RESPONSE)
        viewModel = RootViewModel(userSession, loginService)

        viewModel.observeLiveDataLoginValidation().observeForever(loginValidationObserver)
        EXPECTED_SERVICE_RESPONSE.postValue(ApiResponse(Response.success(LIST_USERS)))
        assertEquals(USER_SUCCESS, viewModel.observeLiveDataLoginValidation().value)
    }

    companion object {
        private val USER_MOCKED = User(1,"userMocked","usermocked@example.com",
            "12345678", "picture","cover","description",
            "location","userMocked","phone")
        private val NO_USER_DETECTED = Resource.error(NetworkSimpleBoundResource.NULL_DATA,null)
        private val USER_SUCCESS = Resource.success(USER_MOCKED,false)
        private val LIST_USERS: List<User> = listOf(USER_MOCKED)
        private val EXPECTED_SERVICE_RESPONSE = MutableLiveData<ApiResponse<List<User>>>()
    }
}