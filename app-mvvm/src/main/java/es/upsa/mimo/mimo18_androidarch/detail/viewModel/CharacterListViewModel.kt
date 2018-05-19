package es.upsa.mimo.mimo18_androidarch.detail.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import es.upsa.mimo.mimo18_androidarch.detail.model.CharacterBindingModel
import es.upsa.mimo.mimo18_androidarch.list.model.CharacterListBindingModel
import es.upsa.mimo.mimo18_androidarch.marvel.repository.MarvelDataSource

class CharacterDetailViewModel(
        application: Application,
        private val marvelApiDataSource: MarvelDataSource
) : AndroidViewModel(application) {

    fun getCharacterDetail(charId : String): LiveData<CharacterBindingModel> {
        return marvelApiDataSource.getCharacterDetail(characterID = charId)
    }

}