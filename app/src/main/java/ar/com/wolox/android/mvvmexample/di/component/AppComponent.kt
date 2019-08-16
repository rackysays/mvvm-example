package ar.com.wolox.android.mvvmexample.di.component

import android.app.Application
import ar.com.wolox.android.mvvmexample.di.module.AppModule
import ar.com.wolox.android.mvvmexample.di.module.ContextModule
import ar.com.wolox.android.mvvmexample.di.module.ViewModelModule
import ar.com.wolox.android.mvvmexample.di.scope.ApplicationScope
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule

@ApplicationScope
@Component(modules = [AndroidSupportInjectionModule::class, AppModule::class,
    ContextModule::class, ViewModelModule::class])
interface AppComponent: AndroidInjector<DaggerApplication> {

    @Component.Builder
    interface Builder{
        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun sharedPreferencesName(sharedPrefName: String): Builder

        fun build(): AppComponent
    }

    override fun inject(instance: DaggerApplication)
}