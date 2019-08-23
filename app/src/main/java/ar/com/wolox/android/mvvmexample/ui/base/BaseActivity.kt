package ar.com.wolox.android.mvvmexample.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import ar.com.wolox.android.mvvmexample.util.ConnectionLiveData
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity: DaggerAppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout())
        init()

        ConnectionLiveData(this).observe(this, Observer {
            changeConnectionStatus(it)
        })
    }

    fun replaceFragment(resId: Int, f: Fragment){
        supportFragmentManager.beginTransaction().replace(resId,f).commit()
    }

    abstract fun init()
    abstract fun layout(): Int

    /**
     * Return the actual network connection status
     */
    open fun changeConnectionStatus(connectionOk : Boolean){}
}