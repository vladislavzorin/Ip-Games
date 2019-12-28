package ru.ipgames.app.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteServersDao {

    @Insert
    fun insert(server: FavoriteServers)

    @get:Query("SELECT idServer FROM favoriteServers ORDER BY id DESC")
    val all: List<String>

    @get:Query("SELECT * FROM favoriteServers ORDER BY id DESC")
    val logall: List<FavoriteServers>

    @Query("SELECT COUNT(idServer) FROM favoriteServers WHERE idServer = :i")
    fun cheakServer(i:String): Int

    @Query("DELETE FROM favoriteServers")
    fun deleteAll()

    @Query("DELETE FROM favoriteServers WHERE idServer = :i")
    fun deleteServer(i:String)
}