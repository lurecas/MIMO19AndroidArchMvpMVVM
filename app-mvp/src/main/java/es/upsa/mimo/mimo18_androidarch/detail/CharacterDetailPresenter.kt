package es.upsa.mimo.mimo18_androidarch.detail

import android.util.Log
import es.upsa.mimo.mimo18_androidarch.marvel.MarvelApi
import es.upsa.mimo.mimo18_androidarch.marvel.MarvelApiConstants
import es.upsa.mimo.mimo18_androidarch.marvel.apiModel.Character
import es.upsa.mimo.mimo18_androidarch.marvel.apiModel.CharactersResponse
import es.upsa.mimo.mimo18_androidarch.util.HashGenerator
import es.upsa.mimo.mimo18_androidarch.util.TimestampProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterDetailPresenter(
        private var api: MarvelApi,
        private var hashGenerator: HashGenerator,
        private var timestampProvider: TimestampProvider
) : CharacterDetailContract.Presenter {

    private var view: CharacterDetailContract.View? = null
    private var characterId: String? = null

    override fun start(view: CharacterDetailContract.View, charId: String) {
        this.view = view
        this.characterId = charId
        fetchMarvelCharacters(characterId = charId)
    }

    override fun start(view: CharacterDetailContract.View) {
        this.view = view
    }

    override fun onRetryLoadButtonClicked() {
        fetchMarvelCharacters(characterId = characterId!!)
    }

    override fun onComicItemClicked(comicName: String) {
        view?.showSnackbarWithText(snackbarText = "You should read : $comicName !")
    }

    override fun onSeriesItemClicked(series: String) {
        view?.showSnackbarWithText(snackbarText = "Don't miss out the $series series")
    }

    override fun onStoryItemClicked(story: String) {
        view?.showSnackbarWithText(snackbarText = "You can find more related stories on $story")
    }

    override fun end() {
        view = null
    }

    private fun fetchMarvelCharacters(characterId: String) {

        view?.hideLoadingIndicator()
        view?.showLoadingIndicator()

        val timestamp = timestampProvider.getTimestamp()

        api.getCharacterDetail(
                characterId = characterId,
                hash = hashGenerator.generate(
                        timestamp = timestamp,
                        privateKey = MarvelApiConstants.PRIVATE_API_KEY,
                        publicKey = MarvelApiConstants.PUBLIC_API_KEY)!!,
                timestamp = timestamp,
                apiKey = MarvelApiConstants.PUBLIC_API_KEY)
                .enqueue(object : Callback<CharactersResponse> {
                    override fun onResponse(call: Call<CharactersResponse>,
                                            response: Response<CharactersResponse>) {

                        view?.hideLoadingIndicator()

                        if (response.isSuccessful) {
                            val characterList = response.body()!!.data!!.results!!.toList()
                            showCharacterData(characterList.first())

                        } else {
                            view?.showCharacterLoadError("Error obtaining character $characterId detail")
                        }

                    }

                    override fun onFailure(call: Call<CharactersResponse>, t: Throwable) {
                        view?.hideLoadingIndicator()
                        view?.showCharacterLoadError("Error obtaining character $characterId detail")
                    }
                })
    }


    private fun showCharacterData(character: Character) {

        // Image
        val thumbnail = character.thumbnail
        view?.showCharacterImage(thumbnail?.path + "." + thumbnail?.extension)

        // Name
        view?.showCharacterName(character.name ?: "")

        // Series
        val seriesNames = character.series?.items?.mapNotNull {
            it.name
        } ?: emptyList()
        view?.showCharacterSeries(seriesNames)

        // Stories
        val storiesNames: List<String> = character.stories?.items?.mapNotNull {
            it.name
        } ?: emptyList()
        view?.showCharacterStories(storiesNames)

        // Comics
        val comicsNames = character.comics?.items?.mapNotNull {
            it.name
        } ?: emptyList()
        view?.showCharacterComics(comicsNames)

    }

}