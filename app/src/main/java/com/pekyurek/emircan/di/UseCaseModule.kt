package com.pekyurek.emircan.di

import com.pekyurek.emircan.data.repository.Repository
import com.pekyurek.emircan.domain.usecase.chatdata.ChatDataUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object UseCaseModule {

    @Provides
    fun provideChatDataUseCase(repository: Repository) = ChatDataUseCase(repository)

}