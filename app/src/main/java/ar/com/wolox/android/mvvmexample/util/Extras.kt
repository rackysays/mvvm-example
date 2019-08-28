package ar.com.wolox.android.mvvmexample.util

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import ar.com.wolox.android.mvvmexample.model.New

/**
 * Util class to store keys to use with [SharedPreferences] or as argument between
 * [Fragment] or [Activity].
 */
object Extras {

    object UserLogin {
        const val USERNAME = "username"
        const val PASSWORD = "password"
        const val USERID = "userid"
        const val LOGGED_TYPE = "loggedType"
    }

    object News {
        const val NEW = "new"
        const val NEW_PICTURE = "picture"
        const val TAG_FULLSCREEN_PICTURE = "fullscreen_picture"
    }
}
