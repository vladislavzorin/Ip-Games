package ru.ipgames.app.model

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import io.reactivex.Flowable

@Dao
interface ServersDao {
    @get:Query("SELECT * FROM servers")
    val all: List<Servers>

    @Query("SELECT * FROM servers")
    fun all(): Flowable<List<Servers>>

    @Insert
    fun insertAll(vararg users: Servers)

    @Query("SELECT COUNT(address) FROM servers WHERE address = :i")
    fun countID(i:String):Int

    @Update
    fun updateAll(vararg users: Servers)

    @Insert
    fun insert(user: Servers)

    @Update
    fun update(user: Servers)

    @Query("DELETE FROM servers")
    fun deleteAll()
}