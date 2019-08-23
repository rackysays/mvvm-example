package ar.com.wolox.android.mvvmexample.di.module

import ar.com.wolox.android.mvvmexample.ui.home.HomePageActivity
import ar.com.wolox.android.mvvmexample.ui.login.LoginActivity
import ar.com.wolox.android.mvvmexample.ui.login.LoginFragment
import ar.com.wolox.android.mvvmexample.ui.root.RootActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AppModule {

    @ContributesAndroidInjector
    internal abstract fun contributeRootActivity(): RootActivity

    @ContributesAndroidInjector
    internal abstract fun contributeLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    internal abstract fun contributeHomePageActivity(): HomePageActivity
}