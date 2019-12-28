package ru.ipgames.app.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.ipgames.app.model.*

@Database(entities = [Server::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): ServerDao
}

@Database(entities = [Servers::class], version = 1)
abstract class AppDatabaseMainServers : RoomDatabase() {
    abstract fun serversDao(): ServersDao
}

@Database(entities = [FavoriteServers::class], version = 1)
abstract class AppDatabaseFavorite : RoomDatabase() {
    abstract fun favoriteServersDao(): FavoriteServersDao
}