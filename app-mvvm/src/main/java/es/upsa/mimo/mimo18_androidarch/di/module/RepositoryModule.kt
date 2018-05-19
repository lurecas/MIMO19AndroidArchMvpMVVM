package es.upsa.mimo.mimo18_androidarch.di.module

import android.arch.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import es.upsa.mimo.mimo18_androidarch.MarvelApplication
import es.upsa.mimo.mimo18_androidarch.list.CharacterViewModelFactory
import es.upsa.mimo.mimo18_androidarch.marvel.MarvelApi
import es.upsa.mimo.mimo18_androidarch.marvel.repository.MarvelDataSource
import es.upsa.mimo.mimo18_androidarch.marvel.repository.MarvelRepository
import es.upsa.mimo.mimo18_androidarch.util.HashGenerator
import es.upsa.mimo.mimo18_androidarch.util.ImageLoader
import es.upsa.mimo.mimo18_androidarch.util.TimestampProvider


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
        return CharacterViewModelFactory(
                marvelDataSource = marvelDataSource,
                application = application,
                imageLoader = imageLoader
        )
    }

}
