package es.upsa.mimo.mimo18_androidarch.list

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import es.upsa.mimo.mimo18_androidarch.marvel.bindingModel.CharacterBindingModel
import es.upsa.mimo.mimo18_androidarch.marvel.repository.MarvelDataSource

class CharacterListViewModel(
        application: Application,
        private val marvelApiDataSource: MarvelDataSource
) : AndroidViewModel(application) {

    fun getCharacterList(): LiveData<List<CharacterBindingModel>> {
        return marvelApiDataSource.getCharacterList()
    }

}