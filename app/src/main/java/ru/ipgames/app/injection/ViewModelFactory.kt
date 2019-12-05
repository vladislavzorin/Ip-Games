package ru.ipgames.app.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import ru.ipgames.app.viewModels.MainFragmentViewModel
import ru.ipgames.app.viewModels.ServersViewModel
import ru.ipgames.app.model.database.AppDatabase
import ru.ipgames.app.model.database.AppDatabaseMainServers


class ViewModelFactory(private val activity: AppCompatActivity): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainFragmentViewModel::class.java)) {
            // заменить на вызов из репозитория или же убрать фабрику на совсем
            val db = Room.databaseBuilder(activity.applicationContext, AppDatabaseMainServers::class.java, "servers").build()

            @Suppress("UNCHECKED_CAST")
            return MainFragmentViewModel(postDao = db.serversDao()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class") as Throwable

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