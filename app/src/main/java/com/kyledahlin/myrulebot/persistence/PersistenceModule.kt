package com.kyledahlin.myrulebot.persistence

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object PersistenceModule {
    @Provides
    @Singleton
    fun providesDao(context: Context): RuleBotDao {
        val database =
            Room.databaseBuilder(context, RuleBotDatabase::class.java, "rulebotdb").build()
        return database.getRuleBotDao()
    }

    @Provides
    @Singleton
    fun providesSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences("rulebotprefs", Context.MODE_PRIVATE)
}