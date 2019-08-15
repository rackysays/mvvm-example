package ar.com.wolox.android.mvvmexample.di.module

import ar.com.wolox.android.mvvmexample.ui.login.LoginActivity
import ar.com.wolox.android.mvvmexample.ui.login.LoginFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AppModule {

    @ContributesAndroidInjector
    internal abstract fun contributeLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment
}