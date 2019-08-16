package ar.com.wolox.android.mvvmexample.di.module

import androidx.annotation.NonNull
import ar.com.wolox.android.mvvmexample.BaseConfiguration
import ar.com.wolox.android.mvvmexample.di.scope.ApplicationScope
import ar.com.wolox.android.mvvmexample.network.LoginService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ApiModule{

    @ApplicationScope
    @Provides
    fun providesHttpLogginInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().
        setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @ApplicationScope
    fun provideHttpClient(@NonNull httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    @ApplicationScope
    fun provideRetrofit(@NonNull okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BaseConfiguration.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @ApplicationScope
    fun provideLoginService(@NonNull retrofit: Retrofit): LoginService =
        retrofit.create(LoginService::class.java)
}