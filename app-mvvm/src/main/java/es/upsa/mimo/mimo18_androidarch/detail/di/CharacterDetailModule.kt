package es.upsa.mimo.mimo18_androidarch.detail.di

import dagger.Module
import dagger.Provides
import es.upsa.mimo.mimo18_androidarch.detail.CharacterDetailPresenter
import es.upsa.mimo.mimo18_androidarch.marvel.MarvelApi
import es.upsa.mimo.mimo18_androidarch.util.HashGenerator
import es.upsa.mimo.mimo18_androidarch.util.ImageLoader
import es.upsa.mimo.mimo18_androidarch.util.TimestampProvider


@Module
class CharacterDetailModule {

    @Provides
    fun providesCharacterDetailPresenter(
            api: MarvelApi,
            hashGenerator: HashGenerator,
            timestampProvider: TimestampProvider,
            imageLoader: ImageLoader
    ): CharacterDetailPresenter {
        return CharacterDetailPresenter(
                api = api,
                hashGenerator = hashGenerator,
                timestampProvider = timestampProvider,
                imageLoader = imageLoader
        )
    }

}
