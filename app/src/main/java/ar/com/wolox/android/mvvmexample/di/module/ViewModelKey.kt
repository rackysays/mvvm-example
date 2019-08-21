package ar.com.wolox.android.mvvmexample.di.module

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
@Target(AnnotationTarget.FUNCTION)
internal annotation class ViewModelKey (val value: KClass<out ViewModel>)