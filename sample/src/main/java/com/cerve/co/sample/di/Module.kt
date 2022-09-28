package com.cerve.co.sample.di

import android.content.Context
import com.cerve.co.bonjour_in_flow.BonjourInFlow
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@[dagger.Module InstallIn(SingletonComponent::class)]
object Module {

    @Singleton
    @Provides
    fun providesTimedBonjourInFlow(
        @ApplicationContext context: Context
    ) = BonjourInFlow(context)

}