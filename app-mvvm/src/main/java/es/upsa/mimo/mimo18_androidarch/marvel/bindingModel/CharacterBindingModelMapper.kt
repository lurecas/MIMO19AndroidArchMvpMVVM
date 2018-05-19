package es.upsa.mimo.mimo18_androidarch.marvel.bindingModel

import es.upsa.mimo.mimo18_androidarch.list.model.CharacterListBindingModel
import es.upsa.mimo.mimo18_androidarch.marvel.apiModel.Character
import es.upsa.mimo.mimo18_androidarch.util.ImageLoader

object CharacterBindingModelMapper {

    fun mapCharacterToCharacterBindingModel(
            character: Character,
            imageLoader: ImageLoader
    ): CharacterBindingModel {

        return character.let {

            // Series
            val seriesNames = character.series?.items?.mapNotNull {
                it.name
            } ?: emptyList()

            // Stories
            val storiesNames: List<String> = character.stories?.items?.mapNotNull {
                it.name
            } ?: emptyList()

            // Comics
            val comicsNames = character.comics?.items?.mapNotNull {
                it.name
            } ?: emptyList()

            CharacterBindingModel(
                    id = it.id ?: "",
                    name = it.name ?: "unnamed",
                    description = it.description,
                    imageUrl = it.thumbnail?.path + "." + it.thumbnail?.extension,
                    series = seriesNames,
                    stories = storiesNames,
                    comics = comicsNames,
                    imageLoader = imageLoader
            )

        }

    }

    fun mapCharacterToCharacterListBindingModel(
            character: Character,
            imageLoader: ImageLoader
    ): CharacterListBindingModel {
        return character.let {

            CharacterListBindingModel(
                    id = it.id ?: "",
                    name = it.name ?: "unnamed",
                    imageUrl = it.thumbnail?.path + "." + it.thumbnail?.extension,
                    imageLoader = imageLoader
            )

        }
    }


}