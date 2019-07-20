package ru.ipgames.app.model

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import io.reactivex.Flowable

@Dao
interface PostDao {
    @get:Query("SELECT * FROM post")
    val all: List<Post>

    @Query("SELECT * FROM post")
    fun all(): Flowable<List<Post>>

    @Insert
    fun insertAll(vararg users: Post)

    @Query("SELECT COUNT(address) FROM post WHERE address = :i")
    fun countID(i:String):Int

    @Update
    fun updateAll(vararg users: Post)

    @Insert
    fun insert(user: Post)

    @Update
    fun update(user: Post)

    @Query("DELETE FROM post")
    fun deleteAll()


}