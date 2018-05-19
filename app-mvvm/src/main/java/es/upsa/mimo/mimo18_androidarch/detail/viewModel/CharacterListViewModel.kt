package es.upsa.mimo.mimo18_androidarch.detail.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import es.upsa.mimo.mimo18_androidarch.detail.model.CharacterBindingModel
import es.upsa.mimo.mimo18_androidarch.marvel.repository.MarvelDataSource
import es.upsa.mimo.mimo18_androidarch.mvvm.Event

class CharacterDetailViewModel(
        application: Application,
        private val marvelApiDataSource: MarvelDataSource
) : AndroidViewModel(application) {

    fun getCharacterDetail(charId: String): LiveData<CharacterBindingModel> {
        return marvelApiDataSource.getCharacterDetail(characterID = charId)
    }

    private val snackBarMessages = MutableLiveData<Event<String>>()

    fun subscribeUserMessages(): LiveData<Event<String>> {
        return snackBarMessages
    }

    fun userClickedOnItem(message: String) {
        snackBarMessages.value = Event(message)
    }

}