package com.rago.callbackflowlocation.di

import android.content.Context
import com.rago.callbackflowlocation.CallbackFlowLocationApplication
import com.rago.callbackflowlocation.data.SharedLocationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideSharedLocationManager(
        @ApplicationContext context: Context
    ): SharedLocationManager =
        SharedLocationManager(
            context,
            (context.applicationContext as CallbackFlowLocationApplication).applicationScope
        )
}