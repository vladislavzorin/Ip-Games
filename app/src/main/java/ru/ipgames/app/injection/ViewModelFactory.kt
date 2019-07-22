package ru.ipgames.app.injection

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.persistence.room.Room
import android.support.v7.app.AppCompatActivity
import io.reactivex.Observable
import ru.ipgames.app.viewModels.ServerListViewModel
import ru.ipgames.app.viewModels.ServersViewModel
import ru.ipgames.app.model.database.AppDatabase
import ru.ipgames.app.model.database.AppDatabaseMainServers


class ViewModelFactory(private val activity: AppCompatActivity): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ServerListViewModel::class.java)) {
            val db = Room.databaseBuilder(activity.applicationContext, AppDatabaseMainServers::class.java, "servers").build()

            @Suppress("UNCHECKED_CAST")
            return ServerListViewModel(postDao = db.serversDao()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}

class ServersModelFactory(private val activity: AppCompatActivity,private val obss:Observable<Int>): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ServersViewModel::class.java)) {
            val db = Room.databaseBuilder(activity.applicationContext, AppDatabase::class.java, "server").build()

            @Suppress("UNCHECKED_CAST")
            return ServersViewModel(serverDao = db.postDao(),obs = obss) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}