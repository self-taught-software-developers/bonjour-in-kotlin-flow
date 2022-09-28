package com.cerve.co.sample.di

import android.content.Context

import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@[dagger.Module InstallIn(SingletonComponent::class)]
object Module {

//    @Singleton
//    @Provides
//    fun providesTimedBonjourInFlow(
//        @ApplicationContext context: Context
//    ) = TimedBonjourInFlow(context)

}