package ar.com.wolox.android.mvvmexample.ui.home

import android.widget.Toast
import ar.com.wolox.android.mvvmexample.R
import ar.com.wolox.android.mvvmexample.ui.base.BaseActivity

class HomePageActivity : BaseActivity(){

    override fun layout(): Int  = R.layout.activity_base

    override fun init() {
        Toast.makeText(this,"WELCOME TO HOMEPAGE ACTIVITY", Toast.LENGTH_LONG).show()
    }

}