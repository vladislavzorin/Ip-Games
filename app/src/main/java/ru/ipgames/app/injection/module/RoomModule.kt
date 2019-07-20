package ru.ipgames.app.injection.module

import android.arch.persistence.room.Room

import dagger.Module
import dagger.Provides
import ru.ipgames.app.activities.PostListActivity
import ru.ipgames.app.model.PostDao
import ru.ipgames.app.model.database.AppDatabase

import javax.inject.Singleton


@Module
@Suppress("unused1")
class RoomModule(){

    @Provides
    @Singleton
     fun provideAppDataBase(activity: PostListActivity): PostDao {
        val db = Room.databaseBuilder(activity.applicationContext, AppDatabase::class.java, "posts").build()
        return db.postDao()
    }
}