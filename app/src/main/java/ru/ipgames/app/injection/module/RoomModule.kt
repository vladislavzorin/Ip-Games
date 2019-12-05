package ru.ipgames.app.injection.module

import androidx.room.Room

import dagger.Module
import dagger.Provides
import ru.ipgames.app.activities.MainActivity
import ru.ipgames.app.model.ServerDao
import ru.ipgames.app.model.database.AppDatabase

import javax.inject.Singleton


@Module
@Suppress("unused1")
class RoomModule(){

    @Provides
    @Singleton
     fun provideAppDataBase(activity: MainActivity): ServerDao {
        val db = Room.databaseBuilder(activity.applicationContext, AppDatabase::class.java, "posts").build()
        return db.postDao()
    }
}