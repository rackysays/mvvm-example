package ar.com.wolox.android.mvvmexample.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ar.com.wolox.android.mvvmexample.factory.ViewModelFactory
import ar.com.wolox.android.mvvmexample.ui.login.LoginViewModel
import ar.com.wolox.android.mvvmexample.ui.root.RootViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule{

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    protected abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RootViewModel::class)
    protected abstract fun bindRootViewModel(rootViewModel: RootViewModel): ViewModel

}