package ar.com.wolox.android.mvvmexample.di.module

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import ar.com.wolox.android.mvvmexample.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class ContextModule{

    @Provides
    @ApplicationScope
    internal fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @ApplicationScope
    internal fun provideSharedPreferences(sharedPrefName: String, context: Context): SharedPreferences {
        return context.getSharedPreferences(sharedPrefName, Activity.MODE_PRIVATE)
    }
}