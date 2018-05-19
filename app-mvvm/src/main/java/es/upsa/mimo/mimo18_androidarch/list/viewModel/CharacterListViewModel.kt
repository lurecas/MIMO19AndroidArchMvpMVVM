package es.upsa.mimo.mimo18_androidarch.list.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import es.upsa.mimo.mimo18_androidarch.list.model.CharacterListBindingModel
import es.upsa.mimo.mimo18_androidarch.marvel.repository.MarvelDataSource

class CharacterListViewModel(
        application: Application,
        private val marvelApiDataSource: MarvelDataSource
) : AndroidViewModel(application) {

    fun getCharacterList(): LiveData<List<CharacterListBindingModel>> {
        return marvelApiDataSource.getCharacterList()
    }

}