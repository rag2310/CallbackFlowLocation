package com.rago.callbackflowlocation.di

import android.content.Context
import com.rago.callbackflowlocation.utils.CheckPermissions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCheckPermissions(@ApplicationContext context: Context): CheckPermissions =
        CheckPermissions(context = context)
}