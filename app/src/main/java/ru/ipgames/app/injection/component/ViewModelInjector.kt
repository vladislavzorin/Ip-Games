package ru.ipgames.app.injection.component

import dagger.Component
import ru.ipgames.app.activities.SearchActivity
import ru.ipgames.app.viewModels.PostListViewModel
import ru.ipgames.app.viewModels.PostViewModel
import ru.ipgames.app.viewModels.ServerInfoViewModel
import ru.ipgames.app.viewModels.ServersViewModel
import ru.ipgames.app.injection.module.NetworkModule
import ru.ipgames.app.injection.module.RoomModule
import javax.inject.Singleton

/**
 * Component providing inject() methods for presenters.
 */
@Singleton
@Component(modules = [(NetworkModule::class),(RoomModule::class)])
interface ViewModelInjector{
    /**
     * Injects required dependencies into the specified PostListViewModel.
     * @param postListViewModel PostListViewModel in which to inject the dependencies
     */
    fun inject(postListViewModel: PostListViewModel)
    /**
     * Injects required dependencies into the specified PostViewModel.
     * @param postViewModel PostViewModel in which to inject the dependencies
     */
    fun inject(postViewModel: PostViewModel)

    fun inject(serversViewModel: ServersViewModel)

    fun inject(serverInfoViewModel: ServerInfoViewModel)

    //fun inject(addYourServerViewModel: AddYourServerViewModel)

    fun inject(searchActivity: SearchActivity)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector
        fun networkModule(networkModule: NetworkModule): Builder
        fun roomModule(RoomModule: RoomModule): Builder
    }
}