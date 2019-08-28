package ar.com.wolox.android.mvvmexample.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity: DaggerAppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout())
        init()
    }

    fun replaceFragment(resId: Int, f: Fragment){
        supportFragmentManager.beginTransaction().replace(resId,f).commit()
    }

    abstract fun init()
    abstract fun layout(): Int
}