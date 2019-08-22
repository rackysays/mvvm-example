package ar.com.wolox.android.mvvmexample.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import ar.com.wolox.android.mvvmexample.model.*
import ar.com.wolox.android.mvvmexample.network.LoginService
import ar.com.wolox.android.mvvmexample.network.utils.ApiResponse
import ar.com.wolox.android.mvvmexample.util.NetworkSimpleBoundResource.NetworkSimpleBoundErrors.NULL_DATA
import ar.com.wolox.android.mvvmexample.util.UserSession
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.Response
import org.mockito.Mockito.`when`

@RunWith(JUnit4::class)
class LoginViewModelTest {

    @Mock lateinit var loginService : LoginService
    @Mock lateinit var userSession : UserSession
    @Mock lateinit var loginValidationObserver: Observer<Resource<User>>

    private lateinit var viewModel: LoginViewModel

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
        `when`(userSession.username).thenReturn(MOCKED_USERNAME)
        viewModel = LoginViewModel(userSession, loginService)
    }

    @Test
    fun userAlreadyStoredIsReturned(){
        assertEquals(MOCKED_USERNAME, viewModel.observeUserStored().value)
    }

    @Test
    fun userIsEmpty() {
        viewModel.onLoginClicked("","12321")
        assertEquals(EMPTY_USERNAME, viewModel.observeValidationLogin().value)
    }

    @Test
    fun passwordIsEmpty() {
        viewModel.onLoginClicked("usermocked@example.com","")
        assertEquals(EMPTY_PASSWORD, viewModel.observeValidationLogin().value)
    }

    @Test
    fun userAndPasswordAreEmpty() {
        viewModel.onLoginClicked("","")
        assertEquals(EMPTY_USERNAME, viewModel.observeValidationLogin().value)
    }

    @Test
    fun wrongUsernameFormat() {
        viewModel.onLoginClicked("userMocked","12345678")
        assertEquals(WRONG_USERNAME, viewModel.observeValidationLogin().value)
    }

    @Test
    fun callingLoginServiceLoading(){
        viewModel.observeLiveDataUserValidation().observeForever(loginValidationObserver)

        `when`(loginService.getUserByLiveCredentials("usermocked@example.com","12345678")).
            thenReturn(MutableLiveData())

        viewModel.onLoginClicked("usermocked@example.com","12345678")
        assertEquals(Resource.loading(null), viewModel.observeLiveDataUserValidation().value)
        viewModel.observeLiveDataUserValidation().removeObserver(loginValidationObserver)
    }

    @Test
    fun callingLoginServiceSuccess(){
        viewModel.observeLiveDataUserValidation().observeForever(loginValidationObserver)

        EXPECTED_SERVICE_RESPONSE.postValue(ApiResponse(Response.success(LIST_USERS)))
        `when`(loginService.getUserByLiveCredentials("usermocked@example.com","12345678")).
            thenReturn(EXPECTED_SERVICE_RESPONSE)

        viewModel.onLoginClicked("usermocked@example.com","12345678")
        assertEquals(USER_SUCCESS, viewModel.observeLiveDataUserValidation().value)
        viewModel.observeLiveDataUserValidation().removeObserver(loginValidationObserver)
    }

    @Test
    fun callingLoginServiceNoUserReturned(){
        viewModel.observeLiveDataUserValidation().observeForever(loginValidationObserver)

        EXPECTED_SERVICE_RESPONSE.postValue(ApiResponse(Response.success(listOf())))
        `when`(loginService.getUserByLiveCredentials("usermocked@example.com","12345678")).
            thenReturn(EXPECTED_SERVICE_RESPONSE)

        viewModel.onLoginClicked("usermocked@example.com","12345678")
        assertEquals(NO_USER_DETECTED, viewModel.observeLiveDataUserValidation().value)
        viewModel.observeLiveDataUserValidation().removeObserver(loginValidationObserver)
    }

    companion object {
        private const val MOCKED_USERNAME = "UserMocked"
        private val EMPTY_USERNAME = ValidationMessage(FormField.USERNAME, ErrorMessage.EMPTY_FIELD)
        private val EMPTY_PASSWORD = ValidationMessage(FormField.PASSWORD, ErrorMessage.EMPTY_FIELD)
        private val WRONG_USERNAME = ValidationMessage(FormField.USERNAME, ErrorMessage.WRONG_FORMAT)
        private val USER_MOCKED = User(1,"userMocked","usermocked@example.com",
            "12345678", "picture","cover","description",
            "location","userMocked","phone")
        private val NO_USER_DETECTED = Resource.error(NULL_DATA,null)
        private val USER_SUCCESS = Resource.success(USER_MOCKED,false)
        private val LIST_USERS: List<User> = listOf(USER_MOCKED)
        private val EXPECTED_SERVICE_RESPONSE = MutableLiveData<ApiResponse<List<User>>>()
    }
}