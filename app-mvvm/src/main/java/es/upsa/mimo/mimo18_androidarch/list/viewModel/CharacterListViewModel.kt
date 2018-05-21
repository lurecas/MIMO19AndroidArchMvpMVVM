package es.upsa.mimo.mimo18_androidarch.list.viewModel

import android.app.Application
import android.arch.core.util.Function
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import es.upsa.mimo.mimo18_androidarch.list.model.CharacterListBindingModel
import es.upsa.mimo.mimo18_androidarch.model.apiModel.Character
import es.upsa.mimo.mimo18_androidarch.model.bindingModel.CharacterBindingModelMapper
import es.upsa.mimo.mimo18_androidarch.model.repository.MarvelDataSource
import es.upsa.mimo.mimo18_androidarch.view.util.ActivityNavigator
import es.upsa.mimo.mimo18_androidarch.view.util.ImageLoader

class CharacterListViewModel(
        application: Application,
        private val marvelApiDataSource: MarvelDataSource,
        private var imageLoader: ImageLoader,
        private val activityNavigator: ActivityNavigator
) : AndroidViewModel(application) {

    fun getCharacterList(): LiveData<List<CharacterListBindingModel>?> {

        return Transformations.map(
                marvelApiDataSource.getCharacterList(),
                Function<List<Character>?, List<CharacterListBindingModel>?> {
                    it?.map {
                        CharacterBindingModelMapper.mapCharacterToCharacterListBindingModel(
                                character = it,
                                imageLoader = imageLoader
                        )
                    }
                }
        )

    }

    fun onCharacterSelected(charId: String) {
        activityNavigator.openCharacterActivity(charId = charId)
    }


}