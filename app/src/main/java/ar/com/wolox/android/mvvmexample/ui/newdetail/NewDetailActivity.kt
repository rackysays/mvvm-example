package ar.com.wolox.android.mvvmexample.ui.newdetail

import ar.com.wolox.android.mvvmexample.R
import ar.com.wolox.android.mvvmexample.ui.base.BaseActivity

class NewDetailActivity : BaseActivity(){

    override fun layout(): Int  = R.layout.activity_base

    override fun init() {
        replaceFragment(R.id.vActivityBaseContent,
            NewDetailFragment().apply { arguments = intent.extras })
    }
}