package es.upsa.mimo.mimo18_androidarch.marvel.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import es.upsa.mimo.mimo18_androidarch.marvel.MarvelApi
import es.upsa.mimo.mimo18_androidarch.marvel.MarvelApiConstants
import es.upsa.mimo.mimo18_androidarch.marvel.apiModel.CharactersResponse
import es.upsa.mimo.mimo18_androidarch.detail.model.CharacterBindingModel
import es.upsa.mimo.mimo18_androidarch.marvel.bindingModel.CharacterBindingModelMapper
import es.upsa.mimo.mimo18_androidarch.list.model.CharacterListBindingModel
import es.upsa.mimo.mimo18_androidarch.util.HashGenerator
import es.upsa.mimo.mimo18_androidarch.util.ImageLoader
import es.upsa.mimo.mimo18_androidarch.util.TimestampProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface MarvelDataSource {

    fun getCharacterList(): LiveData<List<CharacterListBindingModel>>

    fun getCharacterDetail(characterID: String): LiveData<CharacterBindingModel>

    fun searchCharacters(name: String): LiveData<List<CharacterBindingModel>>

}

class MarvelRepository(
        var api: MarvelApi,
        var hashGenerator: HashGenerator,
        var timestampProvider: TimestampProvider,
        var imageLoader: ImageLoader
) : MarvelDataSource {

    override fun getCharacterList(): LiveData<List<CharacterListBindingModel>> {

        val liveData = MutableLiveData<List<CharacterListBindingModel>>()

        val timestamp = timestampProvider.getTimestamp()

        api.getCharacterList(
                hash = hashGenerator.generate(
                        timestamp = timestamp,
                        privateKey = MarvelApiConstants.PRIVATE_API_KEY,
                        publicKey = MarvelApiConstants.PUBLIC_API_KEY)!!,
                timestamp = timestamp,
                apiKey = MarvelApiConstants.PUBLIC_API_KEY
        ).enqueue(object : Callback<CharactersResponse> {
            override fun onResponse(call: Call<CharactersResponse>,
                                    response: Response<CharactersResponse>) {

                if (response.isSuccessful) {

                    response.body()!!
                            .data!!.results!!.toList()
                            .map {
                                CharacterBindingModelMapper
                                        .mapCharacterToCharacterListBindingModel(
                                                character = it,
                                                imageLoader = imageLoader
                                        )
                            }.let {
                                liveData.value = it
                            }

                } else {
                    liveData.value = emptyList()
                }

            }

            override fun onFailure(call: Call<CharactersResponse>, t: Throwable) {
                liveData.value = null
            }
        })

        return liveData
    }

    override fun getCharacterDetail(characterID: String): LiveData<CharacterBindingModel> {
        val liveData = MutableLiveData<CharacterBindingModel>()

        val timestamp = timestampProvider.getTimestamp()

        api.getCharacterDetail(
                hash = hashGenerator.generate(
                        timestamp = timestamp,
                        privateKey = MarvelApiConstants.PRIVATE_API_KEY,
                        publicKey = MarvelApiConstants.PUBLIC_API_KEY)!!,
                timestamp = timestamp,
                apiKey = MarvelApiConstants.PUBLIC_API_KEY,
                characterId = characterID
        ).enqueue(object : Callback<CharactersResponse> {
            override fun onResponse(call: Call<CharactersResponse>,
                                    response: Response<CharactersResponse>) {

                if (response.isSuccessful) {

                    response.body()!!
                            .data!!.results!!.toList()
                            .first()
                            .let {
                                CharacterBindingModelMapper.mapCharacterToCharacterBindingModel(character = it, imageLoader = imageLoader)
                            }.let {
                                liveData.value = it
                            }

                } else {
                    liveData.value = null
                }

            }

            override fun onFailure(call: Call<CharactersResponse>, t: Throwable) {
                liveData.value = null
            }
        })

        return liveData
    }

    override fun searchCharacters(name: String): LiveData<List<CharacterBindingModel>> {

        val liveData = MutableLiveData<List<CharacterBindingModel>>()

        val timestamp = timestampProvider.getTimestamp()

        api.searchCharactersByName(
                hash = hashGenerator.generate(
                        timestamp = timestamp,
                        privateKey = MarvelApiConstants.PRIVATE_API_KEY,
                        publicKey = MarvelApiConstants.PUBLIC_API_KEY)!!,
                timestamp = timestamp,
                apiKey = MarvelApiConstants.PUBLIC_API_KEY,
                query = name
        ).enqueue(object : Callback<CharactersResponse> {
            override fun onResponse(call: Call<CharactersResponse>,
                                    response: Response<CharactersResponse>) {

                if (response.isSuccessful) {

                    response.body()!!
                            .data!!.results!!.toList()
                            .map {
                                CharacterBindingModelMapper.mapCharacterToCharacterBindingModel(character = it, imageLoader = imageLoader)
                            }.let {
                                liveData.value = it
                            }

                } else {
                    liveData.value = emptyList()
                }

            }

            override fun onFailure(call: Call<CharactersResponse>, t: Throwable) {
                liveData.value = null
            }
        })

        return liveData
    }


}
