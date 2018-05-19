package es.upsa.mimo.mimo18_androidarch.detail

import com.nhaarman.mockito_kotlin.argumentCaptor
import es.upsa.mimo.mimo18_androidarch.marvel.MarvelApi
import es.upsa.mimo.mimo18_androidarch.marvel.apiModel.Character
import es.upsa.mimo.mimo18_androidarch.marvel.apiModel.CharacterDataContainer
import es.upsa.mimo.mimo18_androidarch.marvel.apiModel.CharactersResponse
import es.upsa.mimo.mimo18_androidarch.marvel.apiModel.Image
import es.upsa.mimo.mimo18_androidarch.marvel.bindingModel.CharacterBindingModel
import es.upsa.mimo.mimo18_androidarch.util.HashGenerator
import es.upsa.mimo.mimo18_androidarch.util.ImageLoader
import es.upsa.mimo.mimo18_androidarch.util.TimestampProvider
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.mockito.Matchers
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterDetailPresenterTest {

    private fun mockHashGenerator(): HashGenerator {
        return Mockito.mock(HashGenerator::class.java).apply {
            Mockito.`when`(this.generate(
                    timestamp = org.mockito.Matchers.anyLong(),
                    privateKey = org.mockito.Matchers.anyString(),
                    publicKey = org.mockito.Matchers.anyString())).thenReturn("")
        }
    }

    private fun mockSuccesfulApiResponse(charactersResponse: CharactersResponse): MarvelApi {

        val marvelApi = Mockito.mock(MarvelApi::class.java)

        val mockedCall: Call<CharactersResponse> = Mockito.mock(Call::class.java) as Call<CharactersResponse>

        Mockito.`when`(marvelApi.getCharacterDetail(
                hash = Matchers.anyString(),
                timestamp = Matchers.anyLong(),
                apiKey = Matchers.anyString(),
                characterId = Matchers.anyString())).thenReturn(mockedCall)

        Mockito.doAnswer(object : Answer<Any> {
            override fun answer(invocation: InvocationOnMock): Any? {
                val callback = invocation.getArgumentAt(0, Callback::class.java) as Callback<CharactersResponse>
                callback.onResponse(mockedCall, Response.success(charactersResponse))
                return null
            }
        }).`when`(mockedCall).enqueue(Matchers.any(Callback::class.java) as Callback<CharactersResponse>?)

        return marvelApi

    }

    private fun mockErrorApiResponse(): MarvelApi {

        val marvelApi = Mockito.mock(MarvelApi::class.java)

        val mockedCall: Call<CharactersResponse> = Mockito.mock(Call::class.java) as Call<CharactersResponse>

        Mockito.`when`(marvelApi.getCharacterDetail(
                hash = Matchers.anyString(),
                timestamp = Matchers.anyLong(),
                apiKey = Matchers.anyString(),
                characterId = Matchers.anyString())).thenReturn(mockedCall)

        Mockito.doAnswer(object : Answer<Any> {
            override fun answer(invocation: InvocationOnMock): Any? {
                val callback = invocation.getArgumentAt(0, Callback::class.java) as Callback<CharactersResponse>
                callback.onFailure(mockedCall, Throwable())
                return null
            }
        }).`when`(mockedCall).enqueue(Matchers.any(Callback::class.java) as Callback<CharactersResponse>?)

        return marvelApi

    }


    private fun mockCharacterResponse(): CharactersResponse {
        return Character(
                id = TEST_CHAR_ID,
                name = TEST_CHARACTER_NAME,
                description = TEST_CHARACTER_DESCRIPTION,
                thumbnail = Image(
                        path = TEST_CHARACTER_THUMBNAIL_PATH,
                        extension = TEST_CHARACTER_THUMBNAIL_EXTENSION)
        ).let { character ->
            CharacterDataContainer(
                    count = 1,
                    results = arrayOf(character)
            )
        }.let { dataContainer ->
            CharactersResponse(
                    code = 200,
                    data = dataContainer
            )
        }
    }

    private var presenter: CharacterDetailPresenter? = null
    private var detailView: CharacterDetailContract.View = Mockito.mock(CharacterDetailContract.View::class.java)

    private fun buildPresenterWithSuccesfulResponse() {
        presenter = CharacterDetailPresenter(
                api = mockSuccesfulApiResponse(mockCharacterResponse()),
                hashGenerator = mockHashGenerator(),
                timestampProvider = Mockito.mock(TimestampProvider::class.java),
                imageLoader = Mockito.mock(ImageLoader::class.java)
        )
    }

    private fun buildPresenterWithErrorResponse() {
        presenter = CharacterDetailPresenter(
                api = mockErrorApiResponse(),
                hashGenerator = mockHashGenerator(),
                timestampProvider = Mockito.mock(TimestampProvider::class.java),
                imageLoader = Mockito.mock(ImageLoader::class.java)
        )
    }

    @Test
    fun afterApiErrorViewWillShowLoadView() {

        buildPresenterWithErrorResponse()

        presenter?.start(detailView, TEST_CHAR_ID)

        verify(detailView).showCharacterLoadError(Matchers.anyString())

    }

    @Test
    fun presenterWillShowNameAfterSuccesfulResponse() {
        buildPresenterWithSuccesfulResponse()

        presenter?.start(detailView, TEST_CHAR_ID)

        val argumentCaptor = argumentCaptor<CharacterBindingModel>()

        verify(detailView).showCharacter(argumentCaptor.capture())
        assertThat(argumentCaptor.firstValue.name, `is`(TEST_CHARACTER_NAME))

    }


    @Test
    fun presenterWillShowImage() {
        buildPresenterWithSuccesfulResponse()

        presenter?.start(detailView, TEST_CHAR_ID)
        val argumentCaptor = argumentCaptor<CharacterBindingModel>()

        verify(detailView).showCharacter(argumentCaptor.capture())
        assertThat(argumentCaptor.firstValue.imageUrl,
                `is`("$TEST_CHARACTER_THUMBNAIL_PATH.$TEST_CHARACTER_THUMBNAIL_EXTENSION"))

    }

    @Test
    fun presenterWillShowSeries() {
        buildPresenterWithSuccesfulResponse()

        presenter?.start(detailView, TEST_CHAR_ID)

        verify(detailView).showCharacterSeries(Matchers.anyListOf(String::class.java))

    }

    @Test
    fun presenterWillShowStories() {
        buildPresenterWithSuccesfulResponse()

        presenter?.start(detailView, TEST_CHAR_ID)

        verify(detailView).showCharacterStories(Matchers.anyListOf(String::class.java))

    }

    @Test
    fun presenterWillShowComics() {
        buildPresenterWithSuccesfulResponse()

        presenter?.start(detailView, TEST_CHAR_ID)

        verify(detailView).showCharacterComics(Matchers.anyListOf(String::class.java))

    }

    @Test
    fun onComicItemClickedViewWillShowText() {
        buildPresenterWithSuccesfulResponse()
        presenter?.start(detailView, TEST_CHAR_ID)

        presenter?.onComicItemClicked("Comic")

        verify(detailView).showSnackbarWithText(Matchers.anyString())

    }

    @Test
    fun onSeriesItemClickedViewWillShowText() {
        buildPresenterWithSuccesfulResponse()
        presenter?.start(detailView, TEST_CHAR_ID)

        presenter?.onSeriesItemClicked("Series")

        verify(detailView).showSnackbarWithText(Matchers.anyString())

    }

    @Test
    fun onStoryItemClickedViewWillShowText() {
        buildPresenterWithSuccesfulResponse()
        presenter?.start(detailView, TEST_CHAR_ID)

        presenter?.onStoryItemClicked("Comic")

        verify(detailView).showSnackbarWithText(Matchers.anyString())

    }

    companion object {
        private const val TEST_CHAR_ID = "1"
        private const val TEST_CHARACTER_NAME = "Test Name"
        private const val TEST_CHARACTER_DESCRIPTION = "Test Description"
        private const val TEST_CHARACTER_THUMBNAIL_PATH = "https://ep01.epimg.net/internacional/imagenes/2018/05/04/actualidad/1525434241_941769_1525434417_noticia_normal_recorte1"
        private const val TEST_CHARACTER_THUMBNAIL_EXTENSION = "jpg"
    }

}