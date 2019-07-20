package ru.ipgames.app.model.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import ru.ipgames.app.model.Post
import ru.ipgames.app.model.PostDao
import ru.ipgames.app.model.Servers
import ru.ipgames.app.model.ServersDao

@Database(entities = [Post::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}

@Database(entities = [Servers::class], version = 1)
abstract class AppDatabaseMainServers : RoomDatabase() {
    abstract fun serversDao(): ServersDao
}