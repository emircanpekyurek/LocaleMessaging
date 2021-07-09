package com.pekyurek.emircan.di

import android.content.Context
import com.pekyurek.emircan.data.db.dao.MessageDao
import com.pekyurek.emircan.data.db.dao.UserDao
import com.pekyurek.emircan.data.repository.ApiService
import com.pekyurek.emircan.data.repository.RemoteDataSource
import com.pekyurek.emircan.data.repository.Repository
import com.pekyurek.emircan.data.repository.RepositoryImpl
import com.pekyurek.emircan.data.repository.locale.LocaleDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    fun provideLocaleDataSource(userDao: UserDao, messageDao: MessageDao) =
        LocaleDataSource(userDao, messageDao)

    @Provides
    fun provideRemoteDataSource(
        @ApplicationContext context: Context,
        apiService: ApiService
    ) = RemoteDataSource(context, apiService)

    @Provides
    fun provideRepositoryImpl(
        localeDataSource: LocaleDataSource,
        remoteDataSource: RemoteDataSource
    ): Repository = RepositoryImpl(localeDataSource, remoteDataSource)

}