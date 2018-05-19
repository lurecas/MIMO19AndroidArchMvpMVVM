package es.upsa.mimo.mimo18_androidarch.di.module

import android.arch.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import es.upsa.mimo.mimo18_androidarch.MarvelApplication
import es.upsa.mimo.mimo18_androidarch.viewModel.MarvelViewModelFactory
import es.upsa.mimo.mimo18_androidarch.model.MarvelApi
import es.upsa.mimo.mimo18_androidarch.model.repository.MarvelDataSource
import es.upsa.mimo.mimo18_androidarch.model.repository.MarvelRepository
import es.upsa.mimo.mimo18_androidarch.view.util.HashGenerator
import es.upsa.mimo.mimo18_androidarch.view.util.ImageLoader
import es.upsa.mimo.mimo18_androidarch.view.util.TimestampProvider


@Module
class RepositoryModule {

    @Provides
    fun provideMarvelApiService(
            api: MarvelApi,
            hashGenerator: HashGenerator,
            timestampProvider: TimestampProvider
    ): MarvelDataSource {

        return MarvelRepository(
                api = api,
                hashGenerator = hashGenerator,
                timestampProvider = timestampProvider
        )
    }

    @Provides
    fun provideViewModelFactory(
            marvelDataSource: MarvelDataSource,
            application: MarvelApplication,
            imageLoader: ImageLoader
    ): ViewModelProvider.Factory {
        return MarvelViewModelFactory(
                marvelDataSource = marvelDataSource,
                application = application,
                imageLoader = imageLoader
        )
    }

}
