package es.upsa.mimo.mimo18_androidarch.detail.viewModel

import android.app.Application
import android.arch.core.util.Function
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import es.upsa.mimo.mimo18_androidarch.detail.model.CharacterBindingModel
import es.upsa.mimo.mimo18_androidarch.marvel.bindingModel.CharacterBindingModelMapper
import es.upsa.mimo.mimo18_androidarch.marvel.repository.MarvelDataSource
import es.upsa.mimo.mimo18_androidarch.mvvm.Event
import es.upsa.mimo.mimo18_androidarch.util.ImageLoader

class CharacterDetailViewModel(
        application: Application,
        private val marvelApiDataSource: MarvelDataSource,
        var imageLoader: ImageLoader
) : AndroidViewModel(application) {

    fun getCharacterDetail(charId: String): LiveData<CharacterBindingModel?> {

        return Transformations.map(
                marvelApiDataSource.getCharacterDetail(characterID = charId),
                Function { character ->
                    character?.let {
                        CharacterBindingModelMapper.mapCharacterToCharacterBindingModel(
                                character = character,
                                imageLoader = imageLoader
                        )
                    }
                })

    }

    private val snackBarMessages = MutableLiveData<Event<String>>()

    fun subscribeUserMessages(): LiveData<Event<String>> {
        return snackBarMessages
    }

    fun userClickedOnItem(message: String) {
        snackBarMessages.value = Event(message)
    }

}