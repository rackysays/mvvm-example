package ar.com.wolox.android.mvvmexample

import ar.com.wolox.android.mvvmexample.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

class AppController : DaggerApplication(){

    private val appComponent =  DaggerAppComponent.builder()
        .application(this)
        .build()

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = appComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent.inject(instance)
        if (BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object {
        @get:Synchronized
        lateinit var instance: AppController
    }
}