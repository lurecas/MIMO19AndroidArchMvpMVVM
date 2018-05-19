package es.upsa.mimo.mimo18_androidarch.marvel.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import es.upsa.mimo.mimo18_androidarch.marvel.MarvelApi
import es.upsa.mimo.mimo18_androidarch.marvel.MarvelApiConstants
import es.upsa.mimo.mimo18_androidarch.marvel.apiModel.Character
import es.upsa.mimo.mimo18_androidarch.marvel.apiModel.CharactersResponse
import es.upsa.mimo.mimo18_androidarch.util.HashGenerator
import es.upsa.mimo.mimo18_androidarch.util.ImageLoader
import es.upsa.mimo.mimo18_androidarch.util.TimestampProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface MarvelDataSource {

    fun getCharacterList(): LiveData<List<Character>?>

    fun getCharacterDetail(characterID: String): LiveData<Character?>

    fun searchCharacters(name: String): LiveData<List<Character>?>

}

class MarvelRepository(
        var api: MarvelApi,
        var hashGenerator: HashGenerator,
        var timestampProvider: TimestampProvider
) : MarvelDataSource {

    override fun getCharacterList(): LiveData<List<Character>?> {

        val liveData = MutableLiveData<List<Character>?>()

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
                            .let {
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

    override fun getCharacterDetail(characterID: String): LiveData<Character?> {
        val liveData = MutableLiveData<Character?>()

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

    override fun searchCharacters(name: String): LiveData<List<Character>?> {

        val liveData = MutableLiveData<List<Character>?>()

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
                            .let {
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
