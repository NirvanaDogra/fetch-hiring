package com.nirvana.common_network.di

import android.content.Context
import com.nirvana.common_network.EtagInterceptor
import com.nirvana.common_network.ExponentialRetry
import com.nirvana.common_network.NetworkConnectionInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Provides
    @Singleton
    fun loggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        }
    }

    @Provides
    @Singleton
    fun providesETagInterceptor(@ApplicationContext context: Context): EtagInterceptor {
        return EtagInterceptor(context)
    }

    @Provides
    @Singleton
    fun providesNetworkConnectionInterceptor(@ApplicationContext context: Context): NetworkConnectionInterceptor {
        return NetworkConnectionInterceptor(context)
    }

    @Provides
    @Singleton
    fun providesExponentialRetry(): ExponentialRetry {
        return ExponentialRetry()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        eTagInterceptor: EtagInterceptor,
        networkConnectionInterceptor: NetworkConnectionInterceptor,
        exponentialRetry: ExponentialRetry
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
            .addInterceptor(networkConnectionInterceptor)
            .addInterceptor(eTagInterceptor)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(exponentialRetry)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        @Named("BASE_URL") baseUrl: String,
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}