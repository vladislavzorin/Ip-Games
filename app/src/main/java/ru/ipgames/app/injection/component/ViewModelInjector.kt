package ru.ipgames.app.injection.component

import dagger.Component
import ru.ipgames.app.activities.SearchActivity
import ru.ipgames.app.viewModels.GamesFragmentViewModel
import ru.ipgames.app.injection.module.NetworkModule
import ru.ipgames.app.injection.module.RoomModule
import ru.ipgames.app.viewModels.*
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetworkModule::class),(RoomModule::class)])
interface ViewModelInjector{

    fun inject(serverListViewModel: MainFragmentViewModel)

    fun inject(postViewModel: PostViewModel)

    fun inject(serversViewModel: ServersViewModel)

    fun inject(serverInfoViewModel: ServerInfoViewModel)

    //fun inject(addYourServerViewModel: AddYourServerViewModel)

    fun inject(searchActivity: SearchActivity)

    fun inject(hostingsFragmentViewModel: HostingsFragmentViewModel)

    fun inject(gamesFragmentViewModel: GamesFragmentViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector
        fun networkModule(networkModule: NetworkModule): Builder
        fun roomModule(RoomModule: RoomModule): Builder
    }
}