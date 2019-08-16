package ar.com.wolox.android.mvvmexample.util

import ar.com.wolox.android.mvvmexample.di.scope.ApplicationScope
import javax.inject.Inject

@ApplicationScope
class UserSession @Inject
constructor(private val mSharedPreferencesManager: SharedPreferencesManager) {
    // Really, we don't need to query the username because thiExtrass instance live as long as the
    // application, but we should add a check in case Android decides to kill the application
    // and return to a state where this isn't initialized.
    var username: String? = null
        get() {
            if (field == null) {
                field = mSharedPreferencesManager[Extras.UserLogin.USERNAME, ""]
            }
            return field
        }
        set(username) {
            field = username!!
            mSharedPreferencesManager.store(Extras.UserLogin.USERNAME, username)
        }

    var password: String? = null
        get() {
            if (field == null) {
                field = mSharedPreferencesManager[Extras.UserLogin.PASSWORD, ""]
            }
            return field
        }
        set(password) {
            field = password!!
            mSharedPreferencesManager.store(Extras.UserLogin.PASSWORD, password)
        }

    var userId: String? = null
        get() {
            if (field == null) {
                field = mSharedPreferencesManager[Extras.UserLogin.USERID, ""]
            }
            return field
        }
        set(userId) {
            field = userId!!
            mSharedPreferencesManager.store(Extras.UserLogin.USERID, userId)
        }

    var loggedType: String? = null
        get() {
            if (field == null) {
                field = mSharedPreferencesManager[Extras.UserLogin.LOGGED_TYPE, ""]
            }
            return field
        }
        set(loggedType) {
            field = loggedType!!
            mSharedPreferencesManager.store(Extras.UserLogin.LOGGED_TYPE, loggedType)
        }
}