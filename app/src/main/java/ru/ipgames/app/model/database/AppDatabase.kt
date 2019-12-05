package ru.ipgames.app.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.ipgames.app.model.Server
import ru.ipgames.app.model.ServerDao
import ru.ipgames.app.model.Servers
import ru.ipgames.app.model.ServersDao

@Database(entities = [Server::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): ServerDao
}

@Database(entities = [Servers::class], version = 1)
abstract class AppDatabaseMainServers : RoomDatabase() {
    abstract fun serversDao(): ServersDao
}