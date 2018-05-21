package es.upsa.mimo.mimo18_androidarch.viewModel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.v4.util.ArrayMap
import es.upsa.mimo.mimo18_androidarch.MarvelApplication
import es.upsa.mimo.mimo18_androidarch.detail.viewModel.CharacterDetailViewModel
import es.upsa.mimo.mimo18_androidarch.list.viewModel.CharacterListViewModel
import es.upsa.mimo.mimo18_androidarch.model.repository.MarvelDataSource
import es.upsa.mimo.mimo18_androidarch.view.util.ActivityNavigator
import es.upsa.mimo.mimo18_androidarch.view.util.ImageLoader
import java.util.concurrent.Callable

class MarvelViewModelFactory(
        marvelDataSource: MarvelDataSource,
        application: MarvelApplication,
        imageLoader: ImageLoader,
        activityNavigator: ActivityNavigator
        ) : ViewModelProvider.Factory {

    private val creators: ArrayMap<Class<*>, Callable<out ViewModel>> = ArrayMap()

    init {
        creators[CharacterListViewModel::class.java] =
                Callable<ViewModel> { CharacterListViewModel(
                        application = application,
                        marvelApiDataSource = marvelDataSource,
                        imageLoader = imageLoader,
                        activityNavigator = activityNavigator
                ) }

        creators[CharacterDetailViewModel::class.java] =
                Callable<ViewModel> { CharacterDetailViewModel(
                        application = application,
                        marvelApiDataSource = marvelDataSource,
                        imageLoader = imageLoader
                ) }

    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        var creator: Callable<out ViewModel>? = creators[modelClass]
        if (creator == null) {
            for ((key, value) in creators) {
                if (modelClass.isAssignableFrom(key)) {
                    creator = value
                    break
                }
            }
        }
        if (creator == null) {
            throw IllegalArgumentException("Unknown model class $modelClass")
        }
        try {
            return creator.call() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }
}