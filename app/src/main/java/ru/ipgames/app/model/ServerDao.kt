package ru.ipgames.app.model

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import io.reactivex.Flowable

@Dao
interface ServerDao {
    @get:Query("SELECT * FROM server")
    val all: List<Server>

    @Query("SELECT * FROM server")
    fun all(): Flowable<List<Server>>

    @Insert
    fun insertAll(vararg users: Server)

    @Query("SELECT COUNT(address) FROM server WHERE address = :i")
    fun countID(i:String):Int

    @Update
    fun updateAll(vararg users: Server)

    @Insert
    fun insert(user: Server)

    @Update
    fun update(user: Server)

    @Query("DELETE FROM server")
    fun deleteAll()


}