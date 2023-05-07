package com.cerve.co.sample.di

import android.content.Context
import com.cerve.co.bonjour_in_flow.api.BonjourInFlow
import com.cerve.co.bonjour_in_flow.TimedBonjourInFlow
import com.cerve.co.bonjour_in_flow.discover.NetworkServiceDiscoveryUseCase
import com.cerve.co.bonjour_in_flow.resolve.NetworkServiceResolutionUseCase
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@[dagger.Module InstallIn(SingletonComponent::class)]
object Module {

    @Provides
    @Singleton
    fun providesDiscoveryInFlow(
        @ApplicationContext context: Context
    ) = NetworkServiceDiscoveryUseCase(context)

    @Provides
    @Singleton
    fun providesResolutionInFlow(
        @ApplicationContext context: Context
    ) = NetworkServiceResolutionUseCase(context)

//    @Singleton
//    @Provides
//    fun providesBonjourInFlow(
//        @ApplicationContext context: Context
//    ) = BonjourInFlow(context)

    @Singleton
    @Provides
    fun providesTimedBonjourInFlow(
        @ApplicationContext context: Context
    ) = TimedBonjourInFlow(context)

    @Provides
    @Singleton
    fun provideBonjourInFlow(
        @ApplicationContext context: Context
    ) = BonjourInFlow(context)
}