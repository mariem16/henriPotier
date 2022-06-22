package com.test.henripotier

import android.content.Context
import androidx.room.Room
import com.test.henripotier.data.HPDataBase
import com.test.henripotier.data.dao.BookDao
import com.test.henripotier.repository.api.BookRepository
import com.test.henripotier.repository.api.CartRepository
import com.test.henripotier.repository.builder.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    private const val BASE_URL = "https://us-central1-dvt-henri-potier.cloudfunctions.net/app/"


    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS).build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()


    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun providesBookRepository(apiService: ApiService, bookDao : BookDao) = BookRepository(apiService, bookDao)

    @Singleton
    @Provides
    fun providesCartRepository(apiService: ApiService) = CartRepository(apiService)

    @Provides
    fun provideBookDao(hpDataBase : HPDataBase): BookDao {
        return hpDataBase.getBookDao()
    }


    @Provides
    @Singleton
    fun provideHPDataBase(@ApplicationContext appContext: Context): HPDataBase {
        return Room.databaseBuilder(
            appContext,
            HPDataBase::class.java,
            HPDataBase.DB_NAME
        ).build()
    }
}